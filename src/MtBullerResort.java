//import java.io.IOException;
import java.util.ArrayList;

public class MtBullerResort {

	ArrayList<Accommodation> accommodations = new ArrayList<Accommodation>();
	ArrayList<Customer> customers = new ArrayList<Customer>();

	// i don't understand why we need a list of travel
	// packages if they are only made for one person
	// but that's the specification!
	// i would have just linked the travel package to a customer
	ArrayList<TravelPackage> travelPackages = new ArrayList<TravelPackage>();
	
	
//	MtBullerAdmin admin = ;

	public MtBullerResort() {
	}
	
	public void addCustomer(Customer c) {
		customers.add(c);
	}

	public void addTravelPackage(TravelPackage tp) {
		travelPackages.add(tp);
	}
	
	public void addAccommodation(Accommodation a) {
		accommodations.add(a);
	}

//	public void init() throws IOException {
//		
//	}
	
	// for searching the lists without having three separate functions (ew)
	public Identifiable searchByID(ArrayList<Identifiable> ar, Long id) {
		for (Identifiable I : ar) {
			if (I.getID() == id) return I;
		}
		return null;
	}
	
	public Identifiable searchByStringID(ArrayList<Identifiable> ar, String id) {
		int matches = 0;
		Identifiable lastMatch = null;
		for (Identifiable I : ar) {
			if (I.getStringID().toLowerCase().contains(id.toLowerCase())) {
				lastMatch = I;
				matches++;
			}
		}
		// if (matches > 1) println("Too many results! Be more specific with your search query");
		if (matches == 0 || matches > 1) return null;
		return lastMatch;
	}
	

	public ArrayList<Accommodation> getAccommodations() {
		return accommodations;
	}

	public void setAccommodations(ArrayList<Accommodation> accommodations) {
		this.accommodations = accommodations;
	}

	public ArrayList<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(ArrayList<Customer> customers) {
		this.customers = customers;
	}

	public ArrayList<TravelPackage> getTravelPackages() {
		return travelPackages;
	}

	public void setTravelPackages(ArrayList<TravelPackage> travelPackages) {
		this.travelPackages = travelPackages;
	}
}
