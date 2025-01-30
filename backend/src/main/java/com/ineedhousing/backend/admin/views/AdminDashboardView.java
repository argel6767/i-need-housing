package com.ineedhousing.backend.admin.views;

import java.util.List;
import java.util.Optional;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;

import com.ineedhousing.backend.admin.AdminService;
import com.ineedhousing.backend.apis.AirbnbApiService;
import com.ineedhousing.backend.apis.RentCastAPIService;
import com.ineedhousing.backend.housing_listings.HousingListing;
import com.ineedhousing.backend.user.User;
import com.ineedhousing.backend.user.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("/admin")
@PageTitle("Admin Dashboard")
public class AdminDashboardView extends VerticalLayout{
    
    private final AdminService adminService;
    private GridCreator gridBuilder = new GridCreator();
    private final ListingRetrievalForm listingRetrievalForm;


    public AdminDashboardView(AdminService adminService, ListingRetrievalForm listingRetrievalForm) {
        setSizeFull();
        this.adminService = adminService;
        this.listingRetrievalForm = listingRetrievalForm;
        Div main = new Div();
        main.add(new H1("Admin Dashboard"));
        main.add(createGridHorizontalLayout());
        main.setSizeFull();
        main.add(listingRetrievalForm.createApiForm());
        add(main);
    }


    private HorizontalLayout createGridHorizontalLayout() {
        HorizontalLayout grids = new HorizontalLayout();
        Grid<User> userList = gridBuilder.buildUserGrid(adminService.getAllUsers());
        VerticalLayout userLayout = createGridAndLabel(userList, "Users");
        Grid<HousingListing> housingList = gridBuilder.buildHousingGrid(adminService.getAllListings());
        VerticalLayout housingLayout = createGridAndLabel(housingList, "Housings");
        grids.add(List.of(userLayout, housingLayout));
        grids.setWidth("100%");
        grids.setHeight("45%");
        return grids;
    }

    private VerticalLayout createGridAndLabel(Grid<?> grid, String label) {
        VerticalLayout layout = new VerticalLayout();
        H2 gridLabel = new H2(label);
        layout.setWidth("50%");
        layout.add(List.of(gridLabel, grid));
        return layout;
    }

    private class GridCreator {
    
        private Grid<User> buildUserGrid(List<User> users) {
            Grid<User> grid = new Grid<>(User.class, false);
            grid.addColumn(User::getEmail).setHeader("Email");
            grid.addColumn(User::getAuthorities).setHeader("Authorities");
            grid.addColumn(User::getLastLogin).setHeader("Last Logged In");
            grid.addColumn(User::getCreatedAt).setHeader("Created At");
            grid.addColumn(User::getIsEnabled).setHeader("Is Verified");
            grid.setItems(users);
            grid.setWidth("100%");
            return grid;
        }

        private Grid<HousingListing> buildHousingGrid(List<HousingListing> housings) {
            Grid<HousingListing> grid = new Grid<>(HousingListing.class);
            grid.removeColumnByKey("imageUrls");
            grid.removeColumnByKey("coordinates");
            grid.removeColumnByKey("location");
            grid.removeColumnByKey("id");
            grid.addColumn(housing -> {
                Point location = housing.getLocation();
                Coordinate coordinates = location.getCoordinate();
                return String.format("{%.2f, %.2f}", coordinates.getX(), coordinates.getY());
            }).setHeader("Coordinates");
            grid.setItems(housings);
            grid.setWidth("100%");
            return grid;
        }

    }

}
