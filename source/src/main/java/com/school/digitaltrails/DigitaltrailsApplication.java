package com.school.digitaltrails;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestParam;

import helpers.weather.Humidity;
import helpers.weather.Temperature;
import helpers.weather.WindSpeed;
import helpers.weather.TwoHourForecast;
import helpers.weather.TwentyFourHourForecast;
import helpers.weather.FourDaysForecast;
import helpers.misc.Miscellaneous;

import types.weather_api.Now;
import types.attractions.Info;
import types.attractions.Location;
import types.attractions.Coordinates;
import types.attractions_api.AttractionsAPIResponse;
import types.attractions_api.RecommendationsAPIResponse;
import types.errors.ExceptionResponse;
import types.weather_api.Forecast;
import types.weather_api.WeatherAPIResponse;

import java.lang.Math;
import java.util.List;
import java.util.Optional;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.stream.IntStream;
import java.util.ArrayList;
import java.util.Arrays;

import jakarta.servlet.http.HttpServletRequest;

@SpringBootApplication
public class DigitaltrailsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitaltrailsApplication.class, args);
		FirebaseInitialisation firebaseInitialisation = new FirebaseInitialisation();
		firebaseInitialisation.initialize();
	}

	@RestController
	@RequestMapping(path = "/api")
	class BackendController implements ErrorController {

		DigitalTrailsExceptionHandler exceptionHandler;

		@Autowired
		public BackendController(DigitalTrailsExceptionHandler exceptionHandler) {
				this.exceptionHandler = exceptionHandler;
		}

		@Autowired
		private InfoRepository infoRepository;

		@GetMapping("/weather")
		public WeatherAPIResponse weather(@RequestParam("lat") double lat,@RequestParam("lng") double lng) {
			
			Temperature temperatureObj = new Temperature();
			double temperature = temperatureObj.getCurrentTemperature(lat, lng);

			Humidity humidityObj = new Humidity();
			double humidity = humidityObj.getCurrentHumidity(lat, lng);

			WindSpeed windSpeedObj = new WindSpeed();
			double windspeed = windSpeedObj.getCurrentWindSpeed(lat, lng);

			TwoHourForecast twoHourForecastObj = new TwoHourForecast();
			types.weather_forecast_2h_api.Forecast weatherNowcast = twoHourForecastObj.getCurrentWeather(lat, lng);

			TwentyFourHourForecast twentyFourHourForecastObj = new TwentyFourHourForecast();
			Forecast twentyfourhrweatherForecast = twentyFourHourForecastObj.getWeatherForecast(lat, lng);

			FourDaysForecast fourDaysForecastObj = new FourDaysForecast();
			List<Forecast> fourDaysForecast = fourDaysForecastObj.getWeatherForecast(lat, lng);

			WeatherAPIResponse response = new WeatherAPIResponse();

			if(temperature != -1 && humidity != -1 && windspeed != -1 && weatherNowcast != null && twentyfourhrweatherForecast != null && fourDaysForecast.size() >= 4){
				Now now = new Now();
				now.setHumidity(humidity);
				now.setTemperature(temperature);
				now.setWindSpeed(windspeed);
				now.setWeather(weatherNowcast.getForecast());
				response.setCurrentConditions(now);
				response.setTomorrowForecast(twentyfourhrweatherForecast);
				response.setTwoDaysForecast(fourDaysForecast.get(1));
				response.setThreeDayForecast(fourDaysForecast.get(2));
				response.setFourDayForecast(fourDaysForecast.get(3));
				response.setLocation(weatherNowcast.getArea());
				response.setStatus(true);
			}
			System.out.println(response);
			return response;
		}

		@GetMapping("/info")
		public AttractionsAPIResponse attraction_info(@RequestParam("vid") String vid){
			Optional<Info> info = infoRepository.findByVid(vid);
			AttractionsAPIResponse response = new AttractionsAPIResponse();
			if (info.isPresent()) {
				Info info_not_optional = info.get();
				response.setStatus(200);
				response.setInfo(info_not_optional);
			} else {
				response.setStatus(404);
				response.setErrorMessage("No attraction found with that vid");
			}
			return response;
		}

		@GetMapping("/recommendation")
		public RecommendationsAPIResponse recommendation(@RequestParam("lat") double lat,@RequestParam("lng") double lng){
			WeatherAPIResponse weather_now = weather(1.3413, 103.9638);
			String time = "";
			String weather = weather_now.getCurrentConditions().getWeather();
			ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Singapore"));
      int hour = now.getHour();

			if(hour >= 6 && hour < 12){
				time = "morning";
			}
			else if(hour >= 12 && hour < 18){
				time = "afternoon";
			}
			else if(hour >= 18 && hour < 24){
				time = "evening";
			}
			else{
				time = "night";
			}

			if(weather.toLowerCase().replaceAll(" ","").contains("fair") || weather.toLowerCase().replaceAll(" ","").contains("windy")){
				weather = "sunny";
			}else if(weather.toLowerCase().replaceAll(" ","").contains("partlycloudy") || weather.toLowerCase().replaceAll(" ","").contains("cloudy") || weather.toLowerCase().replaceAll(" ","").contains("hazy") || weather.toLowerCase().replaceAll(" ","").contains("mist")){
				weather = "cloudy";
			}else{
				weather = "rainy";
			}
			
			RecommendationsAPIResponse response = new RecommendationsAPIResponse();
			List<String> vids = new ArrayList<>();
			List<String> imageIds = new ArrayList<>();
			List<String> headers = new ArrayList<>();
			List<String> descriptions = new ArrayList<>();
			List<Integer> distances = new ArrayList<>();
			if(time != "night"){
				Optional<List<Info>> infos = infoRepository.findBySuitableComp(time, weather);
				if (infos.isPresent()) {
					vids = infos.get().stream().map(Info::getVid).collect(Collectors.toList());
					imageIds = infos.get().stream().map(Info::getImageId).collect(Collectors.toList());
					headers = infos.get().stream().map(Info::getTitle).collect(Collectors.toList());
					descriptions = infos.get().stream().map(Info::getDescription).collect(Collectors.toList());
					distances = infos.get().stream().map(Info::getLocation).map(Location::getCoordinates).map(coord -> Integer.valueOf( (int) Math.round(Miscellaneous.getDistanceKM(lat, lng, coord.getLat(), coord.getLng())))).collect(Collectors.toList());
				}
			}
			if(response.getImageIds() == null || response.getVids() == null){
				List<Info> recommendations = infoRepository.findRandomAttractions(5);
				vids = recommendations.stream().map(Info::getVid).collect(Collectors.toList());
				imageIds = recommendations.stream().map(Info::getImageId).collect(Collectors.toList());
				headers = recommendations.stream().map(Info::getTitle).collect(Collectors.toList());
				descriptions = recommendations.stream().map(Info::getDescription).collect(Collectors.toList());
				distances = recommendations.stream().map(Info::getLocation).map(Location::getCoordinates).map(coord -> Integer.valueOf( (int) Math.round(Miscellaneous.getDistanceKM(lat, lng, coord.getLat(), coord.getLng())))).collect(Collectors.toList());
			}
			
			List<String> orderedVids = new ArrayList<>();
			List<String> orderedImageIds = new ArrayList<>();
			List<String> orderedHeaders = new ArrayList<>();
			List<String> orderedDescriptions = new ArrayList<>();
			List<Integer> orderedDistances = new ArrayList<>();

			final List<String> finalVids = vids;
			final List<String> finalImageIds = imageIds;
			final List<String> finalHeaders = headers;
			final List<String> finalDescriptions = descriptions;
			final List<Integer> finalDistances = distances;

			IntStream.range(0, finalDistances.size())
			.boxed()
			.sorted(Comparator.comparingInt(finalDistances::get))
			.forEach(i -> {
					String vid = finalVids.get(i);
					String imageId = finalImageIds.get(i);
					String header = finalHeaders.get(i);
					String description = finalDescriptions.get(i);
					Integer distance = finalDistances.get(i);
					orderedVids.add(vid);
					orderedImageIds.add(imageId);
					orderedHeaders.add(header);
					orderedDescriptions.add(description);
					orderedDistances.add(distance);
			});
			
			response.setStatus(200);
			response.setImageIds(orderedImageIds);
			response.setVids(orderedVids);
			response.setHeaders(orderedHeaders);
			response.setDescriptions(orderedDescriptions);
			response.setDistances(orderedDistances);
			return response;
		}
		
		@RequestMapping("/error")
			public ResponseEntity<ExceptionResponse> handleError(HttpServletRequest request) {
					Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
					ResponseEntity<ExceptionResponse> response = exceptionHandler.handleExceptions500(exception);
					return response;
			}
	}
}
