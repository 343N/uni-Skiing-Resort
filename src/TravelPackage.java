import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDate;
//import java.time.temporal.TemporalAmount;
import java.time.format.DateTimeFormatter;

//import Customer.SkillLevel;

public class TravelPackage implements Identifiable, Serializable  {

	static final long serialVersionUID = 1L;

	static double STANDARD_PRICE = 26;
	static long IDcounter = 0;
	
	long ID;

	Accommodation accommodation;
	SkillLevel skillLevel;
	LiftPass passType;
	Customer customer;
	LocalDate startDate;
	LocalDate endDate;
	long durationDays = 0;
	
//	LocalDate liftPassDate;
	long liftPassDuration;
	
	double totalPrice;
	
	short lessons;

	public TravelPackage(String startDate, String endDate) {
		this.startDate = LocalDate.parse(startDate);
		this.endDate = LocalDate.parse(endDate);
		durationDays = this.endDate.toEpochDay() - this.startDate.toEpochDay();
		
	}
	public TravelPackage(LocalDate startDate, LocalDate endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
		durationDays = this.endDate.toEpochDay() - this.startDate.toEpochDay();
		
	}
	
	public void calculateLiftPassType() {
		
		//'season' isn't narrowly defined so i am doing 21 days
		long seasonLength = 21;
		long discountThresholdDays = 5;
		if (liftPassDuration >= discountThresholdDays && liftPassDuration < seasonLength)
			passType = LiftPass.TEN_PERCENT_DISCOUNT;
		else if (liftPassDuration >= seasonLength) 
			passType = LiftPass.SEASON_PASS;
		else 
			passType = LiftPass.STANDARD;		
		
		
		calculatePrice();
	}
	
	public double calculateLiftPassCost() {
		if (passType == null) return 0;

		double passPrice = 0;
		switch (passType){
		case TEN_PERCENT_DISCOUNT:
			passPrice = liftPassDuration * STANDARD_PRICE * 0.9;
			break;
		case STANDARD:
			passPrice = liftPassDuration * STANDARD_PRICE;
			break;
		case SEASON_PASS:
			passPrice = 200;
			break;
		}
		
		return passPrice;
	}
	
	public double calculateLessonFees() {
		if (lessons == 0 || customer == null || customer.getSkillLevel() == null) return 0;
		
//		double lessonPrice = 0;
		switch (customer.getSkillLevel()){
			case EXPERT:
				return lessons * 25;
			case INTERMEDIATE:
				return lessons * 20;
			case BEGINNER:
				return lessons * 15;
			default:
				return 0;
		}
	}
	
	public double calculateAccommodationPrice() {
		return (accommodation == null) ? 0 : accommodation.getPrice() * durationDays;
	}
	
	public void calculatePrice() {
		totalPrice = calculateLiftPassCost() + calculateLessonFees() + calculateAccommodationPrice();
	}

	public Accommodation getAccommodation() {
		return accommodation;
	}

	public void setAccommodation(Accommodation accommodation) {
		this.accommodation = accommodation;
		calculatePrice();
	}

	public SkillLevel getSkillLevel() {
		return skillLevel;
	}

	public void setSkillLevel(SkillLevel skillLevel) {
		this.skillLevel = skillLevel;
		calculatePrice();
	}

	public LiftPass getPassType() {
		return passType;
	}

	public void setPassType(LiftPass passType) {
		this.passType = passType;
		calculatePrice();
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
		calculatePrice();
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
		calculatePrice();
	}
	
	
	public long getID() {
		return ID;
	}
	
	public String getStringID() {
		return null;
	}

	public void setID(long ID) {
		this.ID = ID;
	}
	public void setLiftPassDuration(long d) {
		this.liftPassDuration = d;
		calculatePrice();
	}
	public void setLessons(short d) {
		this.lessons = d;
		calculatePrice();
	}
	
	@Override
	public String toString() {
		String s = "";
		String typestr1 = null;
		String typestr2 = null;
		if (skillLevel != null) typestr1 = skillLevel.toString();
		if (passType != null) typestr2 = passType.toString();
		s += "ID: " + ID;
		if (customer != null) s += "\n\tCustomer: " + customer.getName() + " - ID: " + customer.getID();
		if (accommodation != null) s += "\n\tAccommodation: " + accommodation.getName() + " - ID: " + accommodation.getID();
		s += "\n\tStart Date: "  + startDate.format(DateTimeFormatter.ofPattern("d/M/yy"));
		s += "\n\tEnd Date: "  + endDate.format(DateTimeFormatter.ofPattern("d/M/yy"));
		if (skillLevel != null) 
			s += "\n\tSkill Level: " + typestr1.substring(0,1).toUpperCase() + typestr1.substring(1).toLowerCase();
		if (passType != null) 
			s += "\n\tPass Type: " + typestr1.substring(0,1).toUpperCase() + typestr1.substring(1).toLowerCase();
			s += "\n\tTotal Price: " + new DecimalFormat("###.##").format(totalPrice) + "$";
//		s += "\n\t" + new DecimalFormat("###.##").format(price) + "$ per night.";
		
		
		return s;
	}




	enum LiftPass {
		STANDARD, TEN_PERCENT_DISCOUNT, SEASON_PASS
	}
}
