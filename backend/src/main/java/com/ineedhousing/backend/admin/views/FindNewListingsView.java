package com.ineedhousing.backend.admin.views;

import com.ineedhousing.backend.admin.components.SideNavigation;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("/admin/find-listings")
@PageTitle("Find New Listings")
public class FindNewListingsView extends VerticalLayout {

    private final ListingRetrievalForm listingRetrievalForm;

    public FindNewListingsView(ListingRetrievalForm listingRetrievalForm) {
        setSizeFull();
        this.listingRetrievalForm = listingRetrievalForm;
        Div main = new Div();
        main.add(new H1("Find New Listings"));
        main.add(SideNavigation.getSideNav());
        main.setSizeFull();
        main.add(listingRetrievalForm.createApiForm());
        add(main);
    }

}
