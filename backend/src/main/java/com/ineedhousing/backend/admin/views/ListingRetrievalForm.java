package com.ineedhousing.backend.admin.views;


import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ineedhousing.backend.apis.AirbnbApiService;
import com.ineedhousing.backend.apis.RentCastAPIService;
import com.vaadin.flow.component.ScrollOptions.Alignment;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style.AlignItems;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;



@Component
public class ListingRetrievalForm {

    private final RentCastAPIService rentCastAPIService;
    private final AirbnbApiService airbnbApiService;

    public ListingRetrievalForm(RentCastAPIService rentCastAPIService, AirbnbApiService airbnbApiService) {
        this.rentCastAPIService = rentCastAPIService;
        this.airbnbApiService = airbnbApiService;
    }

    /**
     * Main function
     * @return
     */
    public HorizontalLayout createApiForm() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(List.of(createRentCastLayout(), createAirbnbLayout()));
        horizontalLayout.setPadding(true);
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        horizontalLayout.setSpacing(false);
        horizontalLayout.getThemeList().add("spacing-l");
        return horizontalLayout;
    }

    /**
     * builds RentCast Layout
     * @return
     */
    private VerticalLayout createRentCastLayout() {
        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("50%");
        layout.add(List.of(createRentCastViaLocationCall(), createRentCastViaArea()));
        return layout;
    }

    /**
     * FormLayout calling RentCast API via location parameters
     * @return
     */
    private FormLayout createRentCastViaLocationCall() {
        FormLayout formLayout = new FormLayout();
        H3 label = new H3("RentCast Location Request");
        TextField cityField = new TextField("City");
        TextField stateField = new TextField("State (Abv)");
        Button button = new Button("Retrieve Listing Data");
        button.addClickListener(event -> {
            rentCastAPIService.updateListingsTableViaLocation(cityField.getValue(), stateField.getValue());
        });
        formLayout.add(List.of(label, cityField, stateField, button));
        formLayout.setResponsiveSteps(new ResponsiveStep("0", 1));
        return formLayout;
    }

    /**
     * FormLayout calling RentCast via radius parameters
     * @return
     */
    private FormLayout createRentCastViaArea() {
        FormLayout formLayout = new FormLayout();
        H3 label = new H3("RentCast Area Request");
        NumberField radiusField = new NumberField("Radius");
        NumberField latitudeField = new NumberField("Latitude");
        NumberField longitudeField = new NumberField("Longitude");
        Button button = new Button("Retrieve Listing Data");
        button.addClickListener(event -> {
            rentCastAPIService.updateListingsTableViaArea(radiusField.getValue().intValue(), latitudeField.getValue(), longitudeField.getValue());
        });
        formLayout.add(List.of(label, radiusField, latitudeField, longitudeField, button));
        formLayout.setResponsiveSteps(new ResponsiveStep("0", 1));
        return formLayout;
    }

    /**
     * builds Airbnb layout
     * @return
     */
    private HorizontalLayout createAirbnbLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth("50%");
        layout.add(List.of(createAirbnbViaLocation(), createAirbnbViaGeoCoordinates()));
        return layout;
    }

    /**
     * FormLayout call Airbnb API via location 
     * @return
     */
    private FormLayout createAirbnbViaLocation() {
        FormLayout formLayout = new FormLayout();
        H3 label = new H3("Airbnb Geo-Coordinates Request");
        TextField cityField = new TextField("City");
        DatePicker startDate = new DatePicker("Check In");
        startDate.setMin(LocalDate.now());
        DatePicker endDate = new DatePicker("Check Out");
        endDate.setMin(startDate.getMin().plusDays(1));
        NumberField numOfPets = new NumberField("Pets");
        numOfPets.setMax(5.0);
        numOfPets.setValue(0.0);
        Button button = new Button("Retrieve Listing Data");
        button.addClickListener(event -> {
            airbnbApiService.updateListingViaLocation(cityField.getValue(), startDate.getValue(), endDate.getValue(), numOfPets.getValue().intValue());
        });
        formLayout.add(List.of(label, cityField, startDate, endDate, numOfPets, button));        return formLayout;
    }

    /**
     * FormLayout calling Airbnb API via GeoCoordinates
     * @return
     */
    private FormLayout createAirbnbViaGeoCoordinates() {
        FormLayout formLayout = new FormLayout();
        H3 label = new H3("Airbnb Geo-Coordinates Request");
        NumberField neLatField = new NumberField("North East Latitude");
        NumberField neLongField = new NumberField("North East Longitude");
        NumberField swLatField = new NumberField("South West Latitude");
        NumberField swLongField = new NumberField("South West Longitude");
        DatePicker startDate = new DatePicker("Check In");
        startDate.setMin(LocalDate.now());
        DatePicker endDate = new DatePicker("Check Out");
        endDate.setMin(startDate.getMin().plusDays(1));
        NumberField numOfPets = new NumberField("Pets");
        numOfPets.setMax(5.0);
        numOfPets.setValue(0.0);
        Button button = new Button("Retrieve Listing Data");
        button.addClickListener(event -> {
            airbnbApiService.updateHousingListingsViaGeoCoordinates(neLatField.getValue(), neLongField.getValue(), swLatField.getValue(), swLongField.getValue(), startDate.getValue(), endDate.getValue(), numOfPets.getValue().intValue());
        });
        formLayout.add(List.of(label, neLatField, neLongField, swLatField, swLongField, startDate, endDate, numOfPets, button));
        formLayout.setResponsiveSteps(new ResponsiveStep("0", 1));
        return formLayout;
    }

}
