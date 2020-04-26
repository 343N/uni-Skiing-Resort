import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
//import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class MtBullerAdmin {

	static Scanner sc = new Scanner(System.in);
	static MtBullerResort mbr = new MtBullerResort();
	static String[] customer_names;
	static String[] addresses;
	static String[] accommodation_names;

	static String DATE_FORMAT = "d/M/yy";
	static String DATE_FORMAT_READABLE = "DD/MM/YY";

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		mbr = new MtBullerResort();
//		mbr.init();
		for (int i = 0; i < 20; i++) {
			mbr.addCustomer(generateCustomer());
			if (i % 2 == 0)
				mbr.addAccommodation(generateAccommodation());
		}
		String menu = readTextFile("src/data/menu.txt");
		print(menu);

		while (true) {
			println("\n\n");
			print(menu);
			int option = takeValidNumberRange("\nPlease select an option: ", 1, 13);
			println("");
			switch (option) {
			case 1:
				for (Accommodation a : mbr.getAccommodations()) {
					println(a.toString() + "\n");
				}
				break;
			case 2:
				println("1: Show today's available accommodation");
				println("2: Enter custom date to check availability");
				println("0: Go back\n");
				int suboption = takeValidNumberRange("Please select an option: ", 0, 2);
				int count = 0;
				if (suboption == 0)
					break;
				if (suboption == 1) {

					for (Accommodation a : mbr.getAccommodations()) {
//							LocalDate l = LocalDate.now()
//							count++;
						if (a.isAvailable(LocalDate.now())) {
							println(a.toString() + "\n");
							count++;
						}
					}
					println("There are " + count + " accommodations available for "
							+ LocalDate.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT)) + ".");
				} else if (suboption == 2) {

					LocalDate l = takeValidDateInput("Please enter a date " + DATE_FORMAT_READABLE + ".");
					for (Accommodation a : mbr.getAccommodations()) {
						if (a.isAvailable(l)) {
							count++;
							println(a.toString());
						}
					}
					println("There are " + count + " accommodations available for "
							+ l.format(DateTimeFormatter.ofPattern(DATE_FORMAT)) + ".");
				}
				break;
			case 3:
				println("Adding a customer");
				String fn = takeValidInput("Please enter their full name: ",
						"Invalid input!\nPlease enter their full name:");
				String address = takeValidInput("Please enter their address: ",
						"Invalid input!\nPlease enter their address:");
				// I could do input validation on the phone number but that's a huge hassle, I
				// think.
				String phonenumber = takeValidInput("Please enter their phone number: ",
						"Invalid input!\nPlease enter their phone number:");
				println("Skill Levels:\n1: Beginner\n2: Intermediate\n3: Expert");
				int sl_int = takeValidNumberRange("Please choose their skill level from the list above: ", 1, 3);
				SkillLevel sl;
				switch (sl_int) {
				case 1:
					sl = SkillLevel.BEGINNER;
					break;
				case 2:
					sl = SkillLevel.INTERMEDIATE;
					break;
				case 3:
					sl = SkillLevel.EXPERT;
					break;
				default:
					sl = null;

				}
				Customer c = new Customer(fn, phonenumber, address, sl);
				println("Customer created!");
				println(c.toString() + "\n");
				break;
			case 4:
				Customer cust = null;
				cust = searchCustomer(mbr);
				if (cust == null) break;
				println(cust.toString());
				println("Are you sure you want to remove this customer?");
				println("1: Yes\n2: No");
				int confirmation_option = takeValidNumberRange("Please select an option: ", 1, 2);
				if (confirmation_option == 1) {
					mbr.getCustomers().remove(cust);
					println("Customer removed!");
				} else {
					println("Cancelled.");
				}

				break;
			case 5:
				for (Customer customer : mbr.getCustomers())
					println(customer.toString());
				println("\nThere are currently " + mbr.getCustomers().size() + " customers registered.\n");
				break;
			case 6:
				println("Creating a new travel package..");
				LocalDate startDate = takeValidDateInput("Please enter the starting date of the travel package: ");
				LocalDate endDate = takeValidDateInput("Please enter the ending date of the travel package (in days): ");
				if (endDate.isBefore(startDate)) {
					// flip the dates if the dates aren't in order
					LocalDate temp = startDate;
					startDate = endDate;
					endDate = startDate;
				}
				println("\nDate set!");
				Accommodation a = searchAccommodation(mbr);
				if (a == null) break;
				while (!a.isAvailable(startDate, endDate)) {
					println("Those dates aren't available for this accommodation! Please try another.");
					a = searchAccommodation(mbr);				
					if (a == null) break;	
				}
				if (a == null) break;
				println("Please find the customer who this travel package will belong to.");
				Customer cust1 = null;
				cust1 = searchCustomer(mbr);
				if (cust1 == null) break;
				
				TravelPackage tp = new TravelPackage(startDate, endDate);
				tp.setCustomer(cust1);
				tp.setSkillLevel(cust1.getSkillLevel());

				break;
			case 7:
//				println("Lift Pass");
				println("Please find the package you want to add a lift pass to.");
				TravelPackage tp2 = searchPackage(mbr);
				if (tp2 == null) break;
				println("How many days will the lift pass last?");
				int duration = takeValidNumberRange("Please enter any number, starting with 1: ", 1, Integer.MAX_VALUE);
				tp2.setLiftPassDuration(duration);
				
				println(duration + " day lift pass added to travel package ID " + tp2.getID());
				
				break;
			case 8:
				println("Please find the package you want to add lessons to.");
				TravelPackage tp3 = searchPackage(mbr);
				if (tp3 == null) break;
				println("How many lessons will be in the package?");
				int duration2 = takeValidNumberRange("Please enter any number, starting with 1: ", 1, Integer.MAX_VALUE);
				tp3.setLessons((short) duration2);
				
				println(duration2 + " lessons added to travel package ID " + tp3.getID());
//				println("Price updated to " )
				println(tp3.toString());
				break;
			case 9:
				for (TravelPackage tp4 : mbr.getTravelPackages())
					println(tp4.toString());
				
				println("There are currently " + mbr.getTravelPackages().size() + " travel packages registered.");
				break;
			case 10:
				println("Please find the package you want to remove.");
				TravelPackage tp5 = null;
				tp5 = searchPackage(mbr);
				if (tp5 == null) break;
				println(tp5.toString());
				println("Are you sure you want to remove this travel package?");
				println("1: Yes\n2: No");
				int confirmation_option2 = takeValidNumberRange("Please select an option: ", 1, 2);
				if (confirmation_option2 == 1) {
					mbr.getCustomers().remove(tp5);
					println("Customer removed!");
				} else {
					println("Cancelled.");
				}

				break;
			case 11:
				FileOutputStream fos = new FileOutputStream("packages.dat");
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(mbr.travelPackages);
				
				fos.close();
				oos.close();
				println("Saved to packages.dat!");
				break;
			case 12:
				try {
					FileInputStream fis = new FileInputStream("packages.dat");
					ObjectInputStream ois = new ObjectInputStream(fis);
					mbr.travelPackages = (ArrayList) ois.readObject();
					
					fis.close();
					ois.close();
					
				} catch(FileNotFoundException e) {
					print("File packages.dat doesn't exist!");
					e.printStackTrace();
				}
				println("Loaded packages.dat!");
				break;
			case 13:
				println("See ya!");
				System.exit(0);
				break;
			}
			println("Press ENTER to continue...");
			System.in.read();
		}
//		println(option);
	}

	// for creating random customers and accommodations whenever i start
	public static Customer generateCustomer() throws IOException {
		if (customer_names == null)
			loadCustomerNameList();
		if (addresses == null)
			loadAddressList();

		String name = customer_names[randomInt(customer_names.length)];

		String phone = "04";
		for (int i = 0; i < 8; i++) {
			phone = phone + (int) Math.floor(Math.random() * 10);
		}

		String address = addresses[randomInt(addresses.length / 2, addresses.length - 1)];

		return new Customer(name, phone, address, SkillLevel.values()[randomInt(SkillLevel.values().length)]);
	}
	
	public static Customer searchCustomer(MtBullerResort mbrInstance){
		Customer cust = null;
		boolean cancelled = false;
		while (cust == null) {
			String s1 = takeValidInput("Please enter a name or ID (or cancel to cancel): ",
					"Invalid input!\nPlease enter a customer name or ID (or cancel to cancel): ");
			cancelled = s1.equalsIgnoreCase("cancel");
			cust = (Customer) mbrInstance.searchByID((ArrayList) mbr.getCustomers(), Long.valueOf(s1));
			if (cust == null)
				cust = (Customer) mbrInstance.searchByStringID((ArrayList) mbr.getCustomers(), s1);
			if (cust == null)
				println("We can't find a customer with that ID or name!");
		}
		if (cancelled)
			return null;
		return cust;
		
	}
	
	public static Accommodation searchAccommodation(MtBullerResort mbrInstance) {
		Accommodation cust = null;
		boolean cancelled = false;
		while (cust == null) {
			String s1 = takeValidInput("Please enter a accommodation name or ID (or cancel to cancel): ",
					"Invalid input!\nPlease enter a accommodation name or ID (or cancel to cancel): ");
			cancelled = s1.equalsIgnoreCase("cancel");
			cust = (Accommodation) mbrInstance.searchByID((ArrayList) mbr.getAccommodations(), Long.valueOf(s1));
			if (cust == null)
				cust = (Accommodation) mbrInstance.searchByStringID((ArrayList) mbr.getAccommodations(), s1);
			if (cust == null)
				println("We can't find an accommodation with that ID or name!");
		}
		if (cancelled)
			return null;
		return cust;
		
	}
	
	public static TravelPackage searchPackage(MtBullerResort mbrInstance) {
		TravelPackage cust = null;
		boolean cancelled = false;
		while (cust == null) {
			String s1 = takeValidInput("Please enter an ID (or cancel to cancel): ",
					"Invalid input!\nPlease enter an ID (or cancel to cancel): ");
			cancelled = s1.equalsIgnoreCase("cancel");
			cust = (TravelPackage) mbrInstance.searchByID((ArrayList) mbr.getTravelPackages(), Long.valueOf(s1));
			if (cust == null)
				println("We can't find travel package with that ID!");
		}
		if (cancelled)
			return null;
		return cust;
		
	}

	public static Accommodation generateAccommodation() throws IOException {
		if (accommodation_names == null)
			loadAccommodationNameList();

		String name = accommodation_names[randomInt(accommodation_names.length)];
		String address = addresses[randomInt(addresses.length / 2)];
		String description = "Let's just pretend this is unique.";
		double price = randomInt(50, 400);
		AccommodationType at = AccommodationType.values()[randomInt(AccommodationType.values().length)];

		return new Accommodation(name, address, description, price, at);

	}

	public static void clearScannerBuffer() {
		if (sc != null) {
			sc = new Scanner(System.in);
		}
	}

	public static String takeValidInput(String prompt, String invalidMessage) {
		if (prompt != null && prompt.length() > 0)
			print(prompt);
		clearScannerBuffer();
		boolean valid = false;
		String s = null;
		while (!valid) {
//			s = sc.next();
			if ((s = sc.nextLine()) == null) {
				System.out.println(invalidMessage);
				clearScannerBuffer();
			} else {
				return s;
			}
		}
		return s;
	}

	public static int takeValidNumberInput(String prompt, String invalidMessage) {
		if (prompt != null && prompt.length() > 0)
			print(prompt);
		clearScannerBuffer();
		boolean valid = false;
		int s = 0;
		while (!valid) {
//			sc.nextInt();
			if ((s = sc.nextInt()) == 0) {
				System.out.println(invalidMessage);
				clearScannerBuffer();
			} else {
				return s;
			}
		}
		return s;
	}

	public static int takeValidNumberRange(String prompt, int min, int max) {
		if (prompt != null && prompt.length() > 0)
			print(prompt);
		clearScannerBuffer();
		boolean valid = false;
		int s = -1;
//		sc.nextLine();
		while (!valid) {
//			sc.nextInt();
			if ((s = sc.nextInt()) == -1 || s < min || s > max) {
				System.out.print("Invalid number! Please enter a number between " + min + " and " + max + ": ");
				clearScannerBuffer();
			} else {
				return s;
			}
		}
		return s;
	}

	public static LocalDate takeValidDateInput(String prompt) throws DateTimeParseException {
		if (prompt != null && prompt.length() > 0)
			print(prompt);
		boolean valid = false;
		String s;
		clearScannerBuffer();
		while (!valid) {
//			sc.nextInt();
			if ((s = sc.nextLine()) == null) {
				print("Invalid date! Please enter a date in the format" + DATE_FORMAT_READABLE + ": ");
			} else {
				try {
					LocalDate l = LocalDate.parse(s, DateTimeFormatter.ofPattern(DATE_FORMAT));
					if (!l.isBefore(LocalDate.now()))
						return l;
					else
						print("You can't pick a date before today! ("
								+ LocalDate.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT)) + ")"
								+ "\nPlease enter a date in the format " + DATE_FORMAT_READABLE + ": ");
				} catch (DateTimeParseException dtpe) {
					print("Invalid date! Please enter a date in the format" + DATE_FORMAT_READABLE + ": ");
				}
			}
		}
		return null;
	}

	public static int randomInt(int max) {
		return (int) Math.floor(Math.random() * (max));
	}

	public static int randomInt(int min, int max) {
		int range = max - min;
		return (int) Math.floor((Math.random() * (range)) + min);
	}

	// this is to make generating random things easier
	public static String readTextFile(String path) throws IOException {
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);
		String s = "";
		String line;
		while ((line = br.readLine()) != null) {
			s = s + line + "\n";
		}

		br.close();
		fr.close();
		return s;
	}

//	public static readBinaryFromFile()

	public static void loadCustomerNameList() throws IOException {
		customer_names = readTextFile("src/data/customer_names.txt").split("\n");
	}

	public static void loadAccommodationNameList() throws IOException {
		accommodation_names = readTextFile("src/data/accommodation_names.txt").split("\n");
	}

	public static void loadAddressList() throws IOException {
		String[] street_names = readTextFile("src/data/addresses.txt").split("\n");
		String[] street_types = { "Way", "Court", "Avenue", "Crescent", "Place", "Parade", "Street", "Circuit" };
		String[] new_streets = new String[street_names.length];
		int count = 0;
		for (String s : street_names) {
			String type = street_types[randomInt(street_types.length)];
			s = (int) Math.ceil(Math.random() * 100) + " " + s + " " + type;
			new_streets[count++] = s;
		}

		addresses = new_streets;

	}

	// this is solely to make printing to console less tedious
	public static void print(Object o) {
		System.out.print(o);
	}

	public static void println(Object o) {
		System.out.println(o);
	}

}
