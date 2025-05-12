package com.ineedhousing.backend.admin.views;

import org.springframework.context.annotation.Lazy;

import com.ineedhousing.backend.admin.components.Navigation;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("/admin/find-listings")
@PageTitle("Find New Listings")
@Lazy
public class FindNewListingsView extends VerticalLayout {

    private final ListingRetrievalForm listingRetrievalForm;

    public FindNewListingsView(ListingRetrievalForm listingRetrievalForm) {
        setSizeFull();
        this.listingRetrievalForm = listingRetrievalForm;
        VerticalLayout main = new VerticalLayout();
        main.add(new H1("Find New Listings"));
        main.add(Navigation.getHorizontalNav());
        main.setSizeFull();
        main.add(listingRetrievalForm.createApiForm());
        main.setPadding(true);
        main.setSpacing(true);
        add(main);
    }

}
