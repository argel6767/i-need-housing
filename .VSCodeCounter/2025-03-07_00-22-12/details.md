# Details

Date : 2025-03-07 00:22:12

Directory c:\\Users\\Argel Hernandez\\Desktop\\Projects\\i-need-housing

Total : 173 files,  18890 codes, 1205 comments, 1704 blanks, all 21799 lines

[Summary](results.md) / Details / [Diff Summary](diff.md) / [Diff Details](diff-details.md)

## Files
| filename | language | code | comment | blank | total |
| :--- | :--- | ---: | ---: | ---: | ---: |
| [README.md](/README.md) | Markdown | 30 | 0 | 10 | 40 |
| [backend/.dockerignore](/backend/.dockerignore) | Ignore | 1 | 0 | 0 | 1 |
| [backend/.mvn/wrapper/maven-wrapper.properties](/backend/.mvn/wrapper/maven-wrapper.properties) | Java Properties | 3 | 16 | 1 | 20 |
| [backend/Dockerfile](/backend/Dockerfile) | Docker | 12 | 10 | 9 | 31 |
| [backend/compose.yaml](/backend/compose.yaml) | YAML | 13 | 0 | 1 | 14 |
| [backend/mvnw.cmd](/backend/mvnw.cmd) | Batch | 108 | 26 | 16 | 150 |
| [backend/pom.xml](/backend/pom.xml) | XML | 199 | 1 | 5 | 205 |
| [backend/src/main/java/com/ineedhousing/backend/BackendApplication.java](/backend/src/main/java/com/ineedhousing/backend/BackendApplication.java) | Java | 9 | 0 | 5 | 14 |
| [backend/src/main/java/com/ineedhousing/backend/admin/AdminService.java](/backend/src/main/java/com/ineedhousing/backend/admin/AdminService.java) | Java | 64 | 0 | 13 | 77 |
| [backend/src/main/java/com/ineedhousing/backend/admin/exceptions/UnauthorizedAccessException.java](/backend/src/main/java/com/ineedhousing/backend/admin/exceptions/UnauthorizedAccessException.java) | Java | 6 | 0 | 4 | 10 |
| [backend/src/main/java/com/ineedhousing/backend/admin/views/AdminAuthView.java](/backend/src/main/java/com/ineedhousing/backend/admin/views/AdminAuthView.java) | Java | 64 | 0 | 24 | 88 |
| [backend/src/main/java/com/ineedhousing/backend/admin/views/AdminDashboardView.java](/backend/src/main/java/com/ineedhousing/backend/admin/views/AdminDashboardView.java) | Java | 91 | 0 | 16 | 107 |
| [backend/src/main/java/com/ineedhousing/backend/admin/views/ListingRetrievalForm.java](/backend/src/main/java/com/ineedhousing/backend/admin/views/ListingRetrievalForm.java) | Java | 115 | 28 | 19 | 162 |
| [backend/src/main/java/com/ineedhousing/backend/apis/AirbnbApiService.java](/backend/src/main/java/com/ineedhousing/backend/apis/AirbnbApiService.java) | Java | 127 | 41 | 13 | 181 |
| [backend/src/main/java/com/ineedhousing/backend/apis/ExternalApisController.java](/backend/src/main/java/com/ineedhousing/backend/apis/ExternalApisController.java) | Java | 77 | 23 | 14 | 114 |
| [backend/src/main/java/com/ineedhousing/backend/apis/RentCastAPIService.java](/backend/src/main/java/com/ineedhousing/backend/apis/RentCastAPIService.java) | Java | 112 | 32 | 15 | 159 |
| [backend/src/main/java/com/ineedhousing/backend/apis/exceptions/FailedApiCallException.java](/backend/src/main/java/com/ineedhousing/backend/apis/exceptions/FailedApiCallException.java) | Java | 6 | 0 | 3 | 9 |
| [backend/src/main/java/com/ineedhousing/backend/apis/exceptions/NoListingsFoundException.java](/backend/src/main/java/com/ineedhousing/backend/apis/exceptions/NoListingsFoundException.java) | Java | 6 | 0 | 3 | 9 |
| [backend/src/main/java/com/ineedhousing/backend/apis/requests/AirbnbGeoCoordinateRequest.java](/backend/src/main/java/com/ineedhousing/backend/apis/requests/AirbnbGeoCoordinateRequest.java) | Java | 15 | 0 | 6 | 21 |
| [backend/src/main/java/com/ineedhousing/backend/apis/requests/AirbnbLocationRequest.java](/backend/src/main/java/com/ineedhousing/backend/apis/requests/AirbnbLocationRequest.java) | Java | 14 | 0 | 5 | 19 |
| [backend/src/main/java/com/ineedhousing/backend/apis/requests/AreaRequest.java](/backend/src/main/java/com/ineedhousing/backend/apis/requests/AreaRequest.java) | Java | 12 | 0 | 5 | 17 |
| [backend/src/main/java/com/ineedhousing/backend/apis/requests/CityAndStateRequest.java](/backend/src/main/java/com/ineedhousing/backend/apis/requests/CityAndStateRequest.java) | Java | 11 | 0 | 4 | 15 |
| [backend/src/main/java/com/ineedhousing/backend/auth/AuthenticationController.java](/backend/src/main/java/com/ineedhousing/backend/auth/AuthenticationController.java) | Java | 116 | 26 | 14 | 156 |
| [backend/src/main/java/com/ineedhousing/backend/auth/AuthenticationService.java](/backend/src/main/java/com/ineedhousing/backend/auth/AuthenticationService.java) | Java | 174 | 43 | 23 | 240 |
| [backend/src/main/java/com/ineedhousing/backend/auth/exceptions/AuthenticationException.java](/backend/src/main/java/com/ineedhousing/backend/auth/exceptions/AuthenticationException.java) | Java | 6 | 0 | 2 | 8 |
| [backend/src/main/java/com/ineedhousing/backend/auth/exceptions/ExpiredVerificationCodeException.java](/backend/src/main/java/com/ineedhousing/backend/auth/exceptions/ExpiredVerificationCodeException.java) | Java | 6 | 0 | 2 | 8 |
| [backend/src/main/java/com/ineedhousing/backend/auth/exceptions/UserAlreadyVerifiedException.java](/backend/src/main/java/com/ineedhousing/backend/auth/exceptions/UserAlreadyVerifiedException.java) | Java | 6 | 0 | 2 | 8 |
| [backend/src/main/java/com/ineedhousing/backend/auth/requests/AuthenticateUserDto.java](/backend/src/main/java/com/ineedhousing/backend/auth/requests/AuthenticateUserDto.java) | Java | 11 | 0 | 4 | 15 |
| [backend/src/main/java/com/ineedhousing/backend/auth/requests/ChangePasswordDto.java](/backend/src/main/java/com/ineedhousing/backend/auth/requests/ChangePasswordDto.java) | Java | 12 | 0 | 4 | 16 |
| [backend/src/main/java/com/ineedhousing/backend/auth/requests/ForgotPasswordDto.java](/backend/src/main/java/com/ineedhousing/backend/auth/requests/ForgotPasswordDto.java) | Java | 12 | 0 | 5 | 17 |
| [backend/src/main/java/com/ineedhousing/backend/auth/requests/ResendEmailDto.java](/backend/src/main/java/com/ineedhousing/backend/auth/requests/ResendEmailDto.java) | Java | 10 | 0 | 4 | 14 |
| [backend/src/main/java/com/ineedhousing/backend/auth/requests/VerifyUserDto.java](/backend/src/main/java/com/ineedhousing/backend/auth/requests/VerifyUserDto.java) | Java | 11 | 0 | 5 | 16 |
| [backend/src/main/java/com/ineedhousing/backend/auth/responses/LoginResponse.java](/backend/src/main/java/com/ineedhousing/backend/auth/responses/LoginResponse.java) | Java | 14 | 0 | 6 | 20 |
| [backend/src/main/java/com/ineedhousing/backend/configs/ApplicationConfiguration.java](/backend/src/main/java/com/ineedhousing/backend/configs/ApplicationConfiguration.java) | Java | 39 | 17 | 10 | 66 |
| [backend/src/main/java/com/ineedhousing/backend/configs/RestClientConfiguration.java](/backend/src/main/java/com/ineedhousing/backend/configs/RestClientConfiguration.java) | Java | 51 | 15 | 15 | 81 |
| [backend/src/main/java/com/ineedhousing/backend/configs/SecurityConfiguration.java](/backend/src/main/java/com/ineedhousing/backend/configs/SecurityConfiguration.java) | Java | 53 | 13 | 14 | 80 |
| [backend/src/main/java/com/ineedhousing/backend/email/EmailConfiguration.java](/backend/src/main/java/com/ineedhousing/backend/email/EmailConfiguration.java) | Java | 28 | 6 | 9 | 43 |
| [backend/src/main/java/com/ineedhousing/backend/email/EmailService.java](/backend/src/main/java/com/ineedhousing/backend/email/EmailService.java) | Java | 24 | 7 | 7 | 38 |
| [backend/src/main/java/com/ineedhousing/backend/email/EmailVerificationException.java](/backend/src/main/java/com/ineedhousing/backend/email/EmailVerificationException.java) | Java | 8 | 0 | 4 | 12 |
| [backend/src/main/java/com/ineedhousing/backend/email/InvalidEmailException.java](/backend/src/main/java/com/ineedhousing/backend/email/InvalidEmailException.java) | Java | 6 | 0 | 3 | 9 |
| [backend/src/main/java/com/ineedhousing/backend/favorite\_listings/FavoriteListing.java](/backend/src/main/java/com/ineedhousing/backend/favorite_listings/FavoriteListing.java) | Java | 33 | 3 | 9 | 45 |
| [backend/src/main/java/com/ineedhousing/backend/favorite\_listings/FavoriteListingController.java](/backend/src/main/java/com/ineedhousing/backend/favorite_listings/FavoriteListingController.java) | Java | 56 | 27 | 12 | 95 |
| [backend/src/main/java/com/ineedhousing/backend/favorite\_listings/FavoriteListingRepository.java](/backend/src/main/java/com/ineedhousing/backend/favorite_listings/FavoriteListingRepository.java) | Java | 6 | 0 | 3 | 9 |
| [backend/src/main/java/com/ineedhousing/backend/favorite\_listings/FavoriteListingService.java](/backend/src/main/java/com/ineedhousing/backend/favorite_listings/FavoriteListingService.java) | Java | 53 | 29 | 11 | 93 |
| [backend/src/main/java/com/ineedhousing/backend/favorite\_listings/exceptions/FavoriteListingNotFoundException.java](/backend/src/main/java/com/ineedhousing/backend/favorite_listings/exceptions/FavoriteListingNotFoundException.java) | Java | 6 | 0 | 3 | 9 |
| [backend/src/main/java/com/ineedhousing/backend/favorite\_listings/requests/AddFavoriteListingsRequest.java](/backend/src/main/java/com/ineedhousing/backend/favorite_listings/requests/AddFavoriteListingsRequest.java) | Java | 12 | 0 | 5 | 17 |
| [backend/src/main/java/com/ineedhousing/backend/favorite\_listings/requests/DeleteFavoriteListingsRequest.java](/backend/src/main/java/com/ineedhousing/backend/favorite_listings/requests/DeleteFavoriteListingsRequest.java) | Java | 11 | 0 | 5 | 16 |
| [backend/src/main/java/com/ineedhousing/backend/geometry/GeometrySingleton.java](/backend/src/main/java/com/ineedhousing/backend/geometry/GeometrySingleton.java) | Java | 13 | 10 | 5 | 28 |
| [backend/src/main/java/com/ineedhousing/backend/geometry/PolygonCreator.java](/backend/src/main/java/com/ineedhousing/backend/geometry/PolygonCreator.java) | Java | 15 | 11 | 4 | 30 |
| [backend/src/main/java/com/ineedhousing/backend/housing\_listings/HousingListing.java](/backend/src/main/java/com/ineedhousing/backend/housing_listings/HousingListing.java) | Java | 45 | 2 | 20 | 67 |
| [backend/src/main/java/com/ineedhousing/backend/housing\_listings/HousingListingController.java](/backend/src/main/java/com/ineedhousing/backend/housing_listings/HousingListingController.java) | Java | 102 | 38 | 17 | 157 |
| [backend/src/main/java/com/ineedhousing/backend/housing\_listings/HousingListingRepository.java](/backend/src/main/java/com/ineedhousing/backend/housing_listings/HousingListingRepository.java) | Java | 15 | 0 | 7 | 22 |
| [backend/src/main/java/com/ineedhousing/backend/housing\_listings/HousingListingService.java](/backend/src/main/java/com/ineedhousing/backend/housing_listings/HousingListingService.java) | Java | 84 | 40 | 15 | 139 |
| [backend/src/main/java/com/ineedhousing/backend/housing\_listings/exceptions/NoListingFoundException.java](/backend/src/main/java/com/ineedhousing/backend/housing_listings/exceptions/NoListingFoundException.java) | Java | 6 | 0 | 3 | 9 |
| [backend/src/main/java/com/ineedhousing/backend/housing\_listings/exceptions/NoListingsFoundInAreaException.java](/backend/src/main/java/com/ineedhousing/backend/housing_listings/exceptions/NoListingsFoundInAreaException.java) | Java | 6 | 0 | 3 | 9 |
| [backend/src/main/java/com/ineedhousing/backend/housing\_listings/requests/ExactPreferencesDto.java](/backend/src/main/java/com/ineedhousing/backend/housing_listings/requests/ExactPreferencesDto.java) | Java | 13 | 0 | 5 | 18 |
| [backend/src/main/java/com/ineedhousing/backend/housing\_listings/requests/GetListingsByPreferenceRequest.java](/backend/src/main/java/com/ineedhousing/backend/housing_listings/requests/GetListingsByPreferenceRequest.java) | Java | 11 | 0 | 4 | 15 |
| [backend/src/main/java/com/ineedhousing/backend/housing\_listings/requests/GetListingsBySpecificPreferenceRequest.java](/backend/src/main/java/com/ineedhousing/backend/housing_listings/requests/GetListingsBySpecificPreferenceRequest.java) | Java | 11 | 0 | 5 | 16 |
| [backend/src/main/java/com/ineedhousing/backend/housing\_listings/requests/GetListingsInAreaRequest.java](/backend/src/main/java/com/ineedhousing/backend/housing_listings/requests/GetListingsInAreaRequest.java) | Java | 12 | 0 | 3 | 15 |
| [backend/src/main/java/com/ineedhousing/backend/housing\_listings/requests/HouseListing.java](/backend/src/main/java/com/ineedhousing/backend/housing_listings/requests/HouseListing.java) | Java | 3 | 0 | 3 | 6 |
| [backend/src/main/java/com/ineedhousing/backend/housing\_listings/utils/UserPreferencesFilterer.java](/backend/src/main/java/com/ineedhousing/backend/housing_listings/utils/UserPreferencesFilterer.java) | Java | 59 | 31 | 12 | 102 |
| [backend/src/main/java/com/ineedhousing/backend/jwt/JwtAuthenticationFilter.java](/backend/src/main/java/com/ineedhousing/backend/jwt/JwtAuthenticationFilter.java) | Java | 52 | 5 | 11 | 68 |
| [backend/src/main/java/com/ineedhousing/backend/jwt/JwtService.java](/backend/src/main/java/com/ineedhousing/backend/jwt/JwtService.java) | Java | 69 | 12 | 20 | 101 |
| [backend/src/main/java/com/ineedhousing/backend/user/User.java](/backend/src/main/java/com/ineedhousing/backend/user/User.java) | Java | 73 | 3 | 24 | 100 |
| [backend/src/main/java/com/ineedhousing/backend/user/UserController.java](/backend/src/main/java/com/ineedhousing/backend/user/UserController.java) | Java | 54 | 24 | 10 | 88 |
| [backend/src/main/java/com/ineedhousing/backend/user/UserRepository.java](/backend/src/main/java/com/ineedhousing/backend/user/UserRepository.java) | Java | 8 | 0 | 6 | 14 |
| [backend/src/main/java/com/ineedhousing/backend/user/UserService.java](/backend/src/main/java/com/ineedhousing/backend/user/UserService.java) | Java | 38 | 35 | 11 | 84 |
| [backend/src/main/java/com/ineedhousing/backend/user/UserType.java](/backend/src/main/java/com/ineedhousing/backend/user/UserType.java) | Java | 6 | 0 | 2 | 8 |
| [backend/src/main/java/com/ineedhousing/backend/user/requests/SetUserTypeRequest.java](/backend/src/main/java/com/ineedhousing/backend/user/requests/SetUserTypeRequest.java) | Java | 12 | 0 | 3 | 15 |
| [backend/src/main/java/com/ineedhousing/backend/user\_search\_preferences/UserPreference.java](/backend/src/main/java/com/ineedhousing/backend/user_search_preferences/UserPreference.java) | Java | 70 | 3 | 24 | 97 |
| [backend/src/main/java/com/ineedhousing/backend/user\_search\_preferences/UserPreferenceController.java](/backend/src/main/java/com/ineedhousing/backend/user_search_preferences/UserPreferenceController.java) | Java | 72 | 25 | 15 | 112 |
| [backend/src/main/java/com/ineedhousing/backend/user\_search\_preferences/UserPreferenceRepository.java](/backend/src/main/java/com/ineedhousing/backend/user_search_preferences/UserPreferenceRepository.java) | Java | 6 | 0 | 5 | 11 |
| [backend/src/main/java/com/ineedhousing/backend/user\_search\_preferences/UserPreferenceService.java](/backend/src/main/java/com/ineedhousing/backend/user_search_preferences/UserPreferenceService.java) | Java | 99 | 30 | 16 | 145 |
| [backend/src/main/java/com/ineedhousing/backend/user\_search\_preferences/exceptions/UserPreferenceNotFound.java](/backend/src/main/java/com/ineedhousing/backend/user_search_preferences/exceptions/UserPreferenceNotFound.java) | Java | 6 | 0 | 4 | 10 |
| [backend/src/main/java/com/ineedhousing/backend/user\_search\_preferences/requests/NewFiltersDto.java](/backend/src/main/java/com/ineedhousing/backend/user_search_preferences/requests/NewFiltersDto.java) | Java | 27 | 0 | 4 | 31 |
| [backend/src/main/java/com/ineedhousing/backend/user\_search\_preferences/requests/RawCoordinateUserPreferenceRequest.java](/backend/src/main/java/com/ineedhousing/backend/user_search_preferences/requests/RawCoordinateUserPreferenceRequest.java) | Java | 21 | 0 | 4 | 25 |
| [backend/src/main/java/com/ineedhousing/backend/user\_search\_preferences/requests/RawUserPreferenceRequest.java](/backend/src/main/java/com/ineedhousing/backend/user_search_preferences/requests/RawUserPreferenceRequest.java) | Java | 21 | 0 | 7 | 28 |
| [backend/src/main/java/com/ineedhousing/backend/user\_search\_preferences/utils/UserPreferenceBuilder.java](/backend/src/main/java/com/ineedhousing/backend/user_search_preferences/utils/UserPreferenceBuilder.java) | Java | 75 | 5 | 20 | 100 |
| [backend/src/main/resources/application.properties](/backend/src/main/resources/application.properties) | Java Properties | 25 | 0 | 4 | 29 |
| [backend/src/test/java/com/ineedhousing/backend/BackendApplicationTests.java](/backend/src/test/java/com/ineedhousing/backend/BackendApplicationTests.java) | Java | 9 | 0 | 5 | 14 |
| [backend/src/test/java/com/ineedhousing/backend/admin/AdminServiceTest.java](/backend/src/test/java/com/ineedhousing/backend/admin/AdminServiceTest.java) | Java | 7 | 0 | 4 | 11 |
| [backend/src/test/java/com/ineedhousing/backend/admin/views/AdminAuthViewTest.java](/backend/src/test/java/com/ineedhousing/backend/admin/views/AdminAuthViewTest.java) | Java | 5 | 0 | 6 | 11 |
| [backend/src/test/java/com/ineedhousing/backend/apis/AirbnbApiServiceTest.java](/backend/src/test/java/com/ineedhousing/backend/apis/AirbnbApiServiceTest.java) | Java | 289 | 34 | 52 | 375 |
| [backend/src/test/java/com/ineedhousing/backend/apis/ExternalApisControllerTest.java](/backend/src/test/java/com/ineedhousing/backend/apis/ExternalApisControllerTest.java) | Java | 136 | 21 | 40 | 197 |
| [backend/src/test/java/com/ineedhousing/backend/apis/RentCastApiServiceTest.java](/backend/src/test/java/com/ineedhousing/backend/apis/RentCastApiServiceTest.java) | Java | 184 | 16 | 42 | 242 |
| [backend/src/test/java/com/ineedhousing/backend/auth/AuthenticationControllerTest.java](/backend/src/test/java/com/ineedhousing/backend/auth/AuthenticationControllerTest.java) | Java | 185 | 43 | 38 | 266 |
| [backend/src/test/java/com/ineedhousing/backend/auth/AuthenticationServiceTest.java](/backend/src/test/java/com/ineedhousing/backend/auth/AuthenticationServiceTest.java) | Java | 246 | 43 | 56 | 345 |
| [backend/src/test/java/com/ineedhousing/backend/configs/ApplicationConfigurationTest.java](/backend/src/test/java/com/ineedhousing/backend/configs/ApplicationConfigurationTest.java) | Java | 71 | 12 | 21 | 104 |
| [backend/src/test/java/com/ineedhousing/backend/configs/RestClientConfigurationTest.java](/backend/src/test/java/com/ineedhousing/backend/configs/RestClientConfigurationTest.java) | Java | 54 | 6 | 15 | 75 |
| [backend/src/test/java/com/ineedhousing/backend/configs/SecurityConfigurationTest.java](/backend/src/test/java/com/ineedhousing/backend/configs/SecurityConfigurationTest.java) | Java | 83 | 11 | 23 | 117 |
| [backend/src/test/java/com/ineedhousing/backend/email/EmailConfigurationTest.java](/backend/src/test/java/com/ineedhousing/backend/email/EmailConfigurationTest.java) | Java | 31 | 0 | 7 | 38 |
| [backend/src/test/java/com/ineedhousing/backend/email/EmailServiceTest.java](/backend/src/test/java/com/ineedhousing/backend/email/EmailServiceTest.java) | Java | 38 | 0 | 7 | 45 |
| [backend/src/test/java/com/ineedhousing/backend/favorite\_listings/FavoriteListingControllerTest.java](/backend/src/test/java/com/ineedhousing/backend/favorite_listings/FavoriteListingControllerTest.java) | Java | 111 | 24 | 37 | 172 |
| [backend/src/test/java/com/ineedhousing/backend/favorite\_listings/FavoriteListingServiceTest.java](/backend/src/test/java/com/ineedhousing/backend/favorite_listings/FavoriteListingServiceTest.java) | Java | 119 | 20 | 40 | 179 |
| [backend/src/test/java/com/ineedhousing/backend/geometry/PolygonCreatorTest.java](/backend/src/test/java/com/ineedhousing/backend/geometry/PolygonCreatorTest.java) | Java | 47 | 2 | 19 | 68 |
| [backend/src/test/java/com/ineedhousing/backend/housing\_listings/HousingListingControllerTest.java](/backend/src/test/java/com/ineedhousing/backend/housing_listings/HousingListingControllerTest.java) | Java | 125 | 24 | 40 | 189 |
| [backend/src/test/java/com/ineedhousing/backend/housing\_listings/HousingListingServiceTest.java](/backend/src/test/java/com/ineedhousing/backend/housing_listings/HousingListingServiceTest.java) | Java | 143 | 30 | 39 | 212 |
| [backend/src/test/java/com/ineedhousing/backend/housing\_listings/utils/UserPreferencesFiltererTest.java](/backend/src/test/java/com/ineedhousing/backend/housing_listings/utils/UserPreferencesFiltererTest.java) | Java | 149 | 5 | 40 | 194 |
| [backend/src/test/java/com/ineedhousing/backend/jwt/JwtAuthenticationFilterTest.java](/backend/src/test/java/com/ineedhousing/backend/jwt/JwtAuthenticationFilterTest.java) | Java | 110 | 0 | 14 | 124 |
| [backend/src/test/java/com/ineedhousing/backend/jwt/JwtServiceTest.java](/backend/src/test/java/com/ineedhousing/backend/jwt/JwtServiceTest.java) | Java | 84 | 20 | 27 | 131 |
| [backend/src/test/java/com/ineedhousing/backend/user/UserControllerTest.java](/backend/src/test/java/com/ineedhousing/backend/user/UserControllerTest.java) | Java | 99 | 24 | 34 | 157 |
| [backend/src/test/java/com/ineedhousing/backend/user/UserServiceTest.java](/backend/src/test/java/com/ineedhousing/backend/user/UserServiceTest.java) | Java | 80 | 16 | 26 | 122 |
| [backend/src/test/java/com/ineedhousing/backend/user\_search\_preferences/UserPreferenceControllerTest.java](/backend/src/test/java/com/ineedhousing/backend/user_search_preferences/UserPreferenceControllerTest.java) | Java | 79 | 0 | 31 | 110 |
| [backend/src/test/java/com/ineedhousing/backend/user\_search\_preferences/UserPreferenceServiceTest.java](/backend/src/test/java/com/ineedhousing/backend/user_search_preferences/UserPreferenceServiceTest.java) | Java | 80 | 9 | 23 | 112 |
| [backend/src/test/java/com/ineedhousing/backend/user\_search\_preferences/utils/UserPreferenceBuilderTest.java](/backend/src/test/java/com/ineedhousing/backend/user_search_preferences/utils/UserPreferenceBuilderTest.java) | Java | 120 | 3 | 37 | 160 |
| [frontend/README.md](/frontend/README.md) | Markdown | 23 | 0 | 14 | 37 |
| [frontend/\_\_test\_\_/components/Card.test.jsx](/frontend/__test__/components/Card.test.jsx) | JavaScript JSX | 35 | 0 | 7 | 42 |
| [frontend/\_\_test\_\_/components/Carousel.test.tsx](/frontend/__test__/components/Carousel.test.tsx) | TypeScript JSX | 24 | 3 | 7 | 34 |
| [frontend/\_\_test\_\_/components/Navbar.test.tsx](/frontend/__test__/components/Navbar.test.tsx) | TypeScript JSX | 55 | 0 | 9 | 64 |
| [frontend/\_\_test\_\_/setup.ts](/frontend/__test__/setup.ts) | TypeScript | 31 | 3 | 4 | 38 |
| [frontend/eslint.config.mjs](/frontend/eslint.config.mjs) | JavaScript | 12 | 0 | 5 | 17 |
| [frontend/jest.config.ts](/frontend/jest.config.ts) | TypeScript | 10 | 5 | 4 | 19 |
| [frontend/next.config.ts](/frontend/next.config.ts) | TypeScript | 4 | 1 | 3 | 8 |
| [frontend/package-lock.json](/frontend/package-lock.json) | JSON | 10,887 | 0 | 1 | 10,888 |
| [frontend/package.json](/frontend/package.json) | JSON | 49 | 0 | 1 | 50 |
| [frontend/postcss.config.mjs](/frontend/postcss.config.mjs) | JavaScript | 6 | 1 | 2 | 9 |
| [frontend/public/file.svg](/frontend/public/file.svg) | XML | 1 | 0 | 0 | 1 |
| [frontend/public/globe.svg](/frontend/public/globe.svg) | XML | 1 | 0 | 0 | 1 |
| [frontend/public/sidebar/sidebar-collapse.svg](/frontend/public/sidebar/sidebar-collapse.svg) | XML | 2 | 0 | 0 | 2 |
| [frontend/public/sidebar/sidebar-extend.svg](/frontend/public/sidebar/sidebar-extend.svg) | XML | 2 | 0 | 0 | 2 |
| [frontend/src/app/about/page.tsx](/frontend/src/app/about/page.tsx) | TypeScript JSX | 29 | 0 | 4 | 33 |
| [frontend/src/app/globals.css](/frontend/src/app/globals.css) | CSS | 3 | 0 | 5 | 8 |
| [frontend/src/app/home/Filters.tsx](/frontend/src/app/home/Filters.tsx) | TypeScript JSX | 122 | 21 | 13 | 156 |
| [frontend/src/app/home/InnerFilters.tsx](/frontend/src/app/home/InnerFilters.tsx) | TypeScript JSX | 143 | 22 | 22 | 187 |
| [frontend/src/app/home/page.tsx](/frontend/src/app/home/page.tsx) | TypeScript JSX | 55 | 2 | 7 | 64 |
| [frontend/src/app/layout.tsx](/frontend/src/app/layout.tsx) | TypeScript JSX | 34 | 0 | 7 | 41 |
| [frontend/src/app/page.tsx](/frontend/src/app/page.tsx) | TypeScript JSX | 21 | 0 | 2 | 23 |
| [frontend/src/app/providers.tsx](/frontend/src/app/providers.tsx) | TypeScript JSX | 11 | 0 | 3 | 14 |
| [frontend/src/app/sign-in/page.tsx](/frontend/src/app/sign-in/page.tsx) | TypeScript JSX | 30 | 0 | 2 | 32 |
| [frontend/src/app/sign-up/page.tsx](/frontend/src/app/sign-up/page.tsx) | TypeScript JSX | 30 | 0 | 7 | 37 |
| [frontend/src/app/sign-up/verify/page.tsx](/frontend/src/app/sign-up/verify/page.tsx) | TypeScript JSX | 29 | 0 | 4 | 33 |
| [frontend/src/app/utils/utils.ts](/frontend/src/app/utils/utils.ts) | TypeScript | 5 | 0 | 5 | 10 |
| [frontend/src/components/Card.tsx](/frontend/src/components/Card.tsx) | TypeScript JSX | 23 | 0 | 2 | 25 |
| [frontend/src/components/Carousel.tsx](/frontend/src/components/Carousel.tsx) | TypeScript JSX | 21 | 4 | 1 | 26 |
| [frontend/src/components/Footer.tsx](/frontend/src/components/Footer.tsx) | TypeScript JSX | 13 | 0 | 2 | 15 |
| [frontend/src/components/Form.tsx](/frontend/src/components/Form.tsx) | TypeScript JSX | 113 | 6 | 10 | 129 |
| [frontend/src/components/FormHeader.tsx](/frontend/src/components/FormHeader.tsx) | TypeScript JSX | 16 | 0 | 2 | 18 |
| [frontend/src/components/GlobalContext.tsx](/frontend/src/components/GlobalContext.tsx) | TypeScript JSX | 22 | 5 | 6 | 33 |
| [frontend/src/components/Hero.tsx](/frontend/src/components/Hero.tsx) | TypeScript JSX | 13 | 0 | 1 | 14 |
| [frontend/src/components/HousingSearch.tsx](/frontend/src/components/HousingSearch.tsx) | TypeScript JSX | 55 | 6 | 6 | 67 |
| [frontend/src/components/HousingsList.tsx](/frontend/src/components/HousingsList.tsx) | TypeScript JSX | 74 | 9 | 16 | 99 |
| [frontend/src/components/Loading.tsx](/frontend/src/components/Loading.tsx) | TypeScript JSX | 40 | 1 | 11 | 52 |
| [frontend/src/components/Map.tsx](/frontend/src/components/Map.tsx) | TypeScript JSX | 54 | 7 | 9 | 70 |
| [frontend/src/components/Navbar.tsx](/frontend/src/components/Navbar.tsx) | TypeScript JSX | 54 | 0 | 2 | 56 |
| [frontend/src/components/ResendEmailVerification.tsx](/frontend/src/components/ResendEmailVerification.tsx) | TypeScript JSX | 17 | 0 | 3 | 20 |
| [frontend/src/components/VerificationCode.tsx](/frontend/src/components/VerificationCode.tsx) | TypeScript JSX | 123 | 4 | 11 | 138 |
| [frontend/src/endpoints/apiConfig.ts](/frontend/src/endpoints/apiConfig.ts) | TypeScript | 27 | 2 | 5 | 34 |
| [frontend/src/endpoints/auths.ts](/frontend/src/endpoints/auths.ts) | TypeScript | 109 | 32 | 10 | 151 |
| [frontend/src/endpoints/favorites.ts](/frontend/src/endpoints/favorites.ts) | TypeScript | 60 | 25 | 7 | 92 |
| [frontend/src/endpoints/listings.ts](/frontend/src/endpoints/listings.ts) | TypeScript | 66 | 23 | 6 | 95 |
| [frontend/src/endpoints/preferences.ts](/frontend/src/endpoints/preferences.ts) | TypeScript | 56 | 20 | 6 | 82 |
| [frontend/src/hooks/hooks.ts](/frontend/src/hooks/hooks.ts) | TypeScript | 22 | 2 | 2 | 26 |
| [frontend/src/interfaces/entities.ts](/frontend/src/interfaces/entities.ts) | TypeScript | 52 | 0 | 3 | 55 |
| [frontend/src/interfaces/requests/authsRequests.ts](/frontend/src/interfaces/requests/authsRequests.ts) | TypeScript | 21 | 0 | 4 | 25 |
| [frontend/src/interfaces/requests/favoriteListingsRequests.ts](/frontend/src/interfaces/requests/favoriteListingsRequests.ts) | TypeScript | 4 | 0 | 1 | 5 |
| [frontend/src/interfaces/requests/housingListingRequests.ts](/frontend/src/interfaces/requests/housingListingRequests.ts) | TypeScript | 10 | 0 | 2 | 12 |
| [frontend/src/interfaces/requests/userPreferencesRequests.ts](/frontend/src/interfaces/requests/userPreferencesRequests.ts) | TypeScript | 12 | 0 | 1 | 13 |
| [frontend/src/interfaces/requests/userRequests.ts](/frontend/src/interfaces/requests/userRequests.ts) | TypeScript | 4 | 0 | 0 | 4 |
| [frontend/src/interfaces/responses/authsResponses.ts](/frontend/src/interfaces/responses/authsResponses.ts) | TypeScript | 4 | 0 | 0 | 4 |
| [frontend/tailwind.config.ts](/frontend/tailwind.config.ts) | TypeScript | 51 | 0 | 3 | 54 |
| [frontend/tsconfig.json](/frontend/tsconfig.json) | JSON with Comments | 27 | 0 | 1 | 28 |
| [openapi/openapi.yml](/openapi/openapi.yml) | YAML | 22 | 0 | 7 | 29 |
| [openapi/parameters/email.yaml](/openapi/parameters/email.yaml) | YAML | 8 | 0 | 0 | 8 |
| [openapi/paths/auths.yaml](/openapi/paths/auths.yaml) | YAML | 0 | 0 | 1 | 1 |
| [openapi/paths/favorites.yaml](/openapi/paths/favorites.yaml) | YAML | 0 | 0 | 1 | 1 |
| [openapi/paths/gather-housings.yaml](/openapi/paths/gather-housings.yaml) | YAML | 0 | 0 | 1 | 1 |
| [openapi/paths/users/email.yaml](/openapi/paths/users/email.yaml) | YAML | 33 | 0 | 6 | 39 |
| [openapi/schemas/favoriteListing.yaml](/openapi/schemas/favoriteListing.yaml) | YAML | 12 | 0 | 1 | 13 |
| [openapi/schemas/listing.yaml](/openapi/schemas/listing.yaml) | YAML | 75 | 0 | 1 | 76 |
| [openapi/schemas/user.yaml](/openapi/schemas/user.yaml) | YAML | 55 | 0 | 1 | 56 |
| [openapi/schemas/userPreferences.yaml](/openapi/schemas/userPreferences.yaml) | YAML | 111 | 0 | 1 | 112 |
| [scripts/inject\_and\_run\_backend.py](/scripts/inject_and_run_backend.py) | Python | 40 | 1 | 12 | 53 |
| [scripts/push\_backend\_image.py](/scripts/push_backend_image.py) | Python | 45 | 0 | 10 | 55 |

[Summary](results.md) / Details / [Diff Summary](diff.md) / [Diff Details](diff-details.md)