import java.io.*;
import java.util.*;

/* ResourceManagement
 *
 * Stores the information needed to decide which items to purchase for the given budget and departments
 */
public class ResourceManagement
{
    private PriorityQueue<Department> departmentPQ; /* priority queue of departments */
    private Double remainingBudget;                 /* the budget left after purchases are made (should be 0 after the constructor runs) */
    private Double budget;                          /* the total budget allocated */
     private ArrayList<Department> departmentsList; 

  /* TO BE COMPLETED BY YOU
   * Fill in your name in the function below
   */  
  public static void printNames( )
  {
    /* TODO : Fill in your name and your partner's name */
    System.out.println("This solution was completed by:");
    System.out.println("<Bryan Salyer>");
    System.out.println("<student name #2 (if no partner write \"N/A\")>");
  }

  /* Constructor for a ResourceManagement object
   * TODO
   * Simulates the algorithm from the pdf to determine what items are purchased
   * for the given budget and department item lists.
   */
  public ResourceManagement( ArrayList<String> fileNames, Double budget )
  {
    /* Create a department for each file listed in fileNames */
    this.budget = budget;
    this.remainingBudget = budget;
    this.departmentPQ = new PriorityQueue<Department>();
    this.departmentsList = new ArrayList<Department>();

    //Looping through each of the departmernts making department objects
    for (String f : fileNames) {
      Department d = new Department(f);
      if (d.priority == null) d.priority = 0.0;
      departmentPQ.add(d);
      departmentsList.add(d);
    }
  
    
  }
  
  /* Provided code - DO NOT CHANGE THIS METHOD 
   * findAndPrintBudget
   * Executes the budget distribution algorithm and prints a record of which items were purchased
   */    
  public void findAndPrintBudget(  ){
    /* Simulate the algorithm from the PDF */
    allocateBudget(  );
    System.out.print( "\n\n" );
    
    /* Summary of how the budget was distributed to the departments */
    printSummaryOfDistribution(  );
  } 

  /* allocateBudget
   * TODO
   * Executes the budget distribution algorithm and prints a record of which items were purchased
   */    
  private void allocateBudget(  ){
    /* Simulate the algorithm for picking the items to purchase */
    /* Print a record of each item as they are purchased (see PDF and sample output for example tables) */
System.out.println("ITEMS PURCHASED---------------------------");

//Allows purchace of items as long as the budget remains for the department
 while (remainingBudget > 0.0000001 && !departmentPQ.isEmpty()) {
      Department dept = departmentPQ.poll();
      
      //Removes any of the objects that are to expensive for the budget
      while (dept.itemsDesired.peek() != null && dept.itemsDesired.peek().price > remainingBudget) {
        //moves items to the removed list
        dept.itemsRemoved.add(dept.itemsDesired.poll());
      }

      //If there are no objects left makes them have a scholorship
      if (dept.itemsDesired.peek() == null) {
        //Caps the budget at $1000
        double scholarship = Math.min(1000.0, remainingBudget);
        //If under 1000 reinserts into the department
        if (scholarship <= 0.0000001) {
          departmentPQ.add(dept);
          break;
        }
        //Creates the scholarship as an item, adds it to the list, updates the total, and deducts from the budget.
        Item scholarshipItem = new Item("Scholarship", scholarship);
        dept.itemsReceived.add(scholarshipItem);
        dept.priority += scholarship;
        remainingBudget -= scholarship;

        //Prints out the purchaced record for the scholarship
        String price = String.format("$%.2f", scholarshipItem.price );
        System.out.printf("Department of %-30s- %-30s- %30s\n", dept.name, scholarshipItem.name, price );
        //Reinserts the department into the queue with an updated priority
departmentPQ.add(dept);
      } else {
        //When the department has affordable items left
        Item nextItem = dept.itemsDesired.poll(); //Get next item
        dept.itemsReceived.add(nextItem); //Mark as purchaced
        dept.priority += nextItem.price; //Increase total spending
        remainingBudget -= nextItem.price; //Decrease budget


        //Prints out purchase record for this item
        String price = String.format("$%.2f", nextItem.price );
        System.out.printf("Department of %-30s- %-30s- %30s\n", dept.name, nextItem.name, price );
        //Reinserts the department back into the queue for the next round of purchaces
departmentPQ.add(dept);
  } 
}
  }

  /* printSummaryOfDistribution
   * TODO
   * Print a summary of what each department received and did not receive
   */    
  private void printSummaryOfDistribution(  ){
System.out.println("----------------------------------------------------------------");

//loops through each department to display what they did and didn't receive
    for (Department dept : departmentsList) {
      System.out.println(dept.name);

      String totalSpentStr = String.format("$%.2f", dept.priority);
      System.out.print("Total Spent");
      System.out.println(" = " + totalSpentStr);

      //Calculates the percentage of the total budget spent on each department
      double percent = 0.0;
      if (budget != null && budget > 0.0000001) percent = (dept.priority / budget) * 100.0;
      String percentStr = String.format("%.2f", percent);
      System.out.println("Percent of Budget = " + percentStr + "%");

      //displays the receved items
      System.out.println("---------------------------");
      System.out.println("ITEMS RECEIVED");
      if (dept.itemsReceived.isEmpty()) {
        System.out.println(" None");
      } else {
        for (Item item : dept.itemsReceived) {
          String price = String.format("$%.2f", item.price );
          System.out.printf("%-30s- %30s\n", item.name, price );
        }
      }

      //displays items that were not receved both removed and remaining
      System.out.println("ITEMS NOT RECEIVED");
      boolean printedAnyNotReceived = false;

      //prints the removed items
      for (Item item : dept.itemsRemoved) {
        String price = String.format("$%.2f", item.price );
        System.out.printf("%-30s- %30s\n", item.name, price );
        printedAnyNotReceived = true;
      }

      //prints any still needed items that weren't bought
      for (Item item : dept.itemsDesired) {
        String price = String.format("$%.2f", item.price );
        System.out.printf("%-30s- %30s\n", item.name, price );
        printedAnyNotReceived = true;
      }

      if (!printedAnyNotReceived) {
        System.out.println(" None");
      }
      System.out.println();
    }
  } 
}

/* Department
 *
 * Stores the information associated with a Department at the university
 */
class Department implements Comparable<Department>
{
  String name;                /* name of this department */
  Double priority;            /* total money spent on this department */
  Queue<Item> itemsDesired;   /* list of items this department wants */
  Queue<Item> itemsReceived;  /* list of items this department received */
  Queue<Item> itemsRemoved;   /* list of items that were skipped because they exceeded the remaining budget */

  /* TODO
   * Constructor to build a Department from the information in the given fileName
   */
  public Department( String fileName ){

    /* Open the fileName, create items based on the contents, and add those items to itemsDesired */
this.itemsDesired = new LinkedList<Item>(); //Queue to holdwanted items
    this.itemsReceived = new LinkedList<Item>(); //Queue to track purchaced items
    this.itemsRemoved = new LinkedList<Item>(); //Queue to track the skipped items
    this.priority = 0.0; //Makes it start with zero total spending

    File file = new File(fileName);
    Scanner input;
    try {
      input = new Scanner(file); //Opens the file
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("The file "+fileName+" was not found.");
      this.name = fileName; //Uses the filename as a fallback name
      return;
    }

    //The first token is the departments name
    if (input.hasNext()) {
      this.name = input.next();
    } else {
      this.name = fileName;
    }

    //Remaining tokens as a alternate between item names and the prices
    while (input.hasNext()) {
      String itemName = input.next();
      if (!input.hasNextDouble()) {
      break; //stops if there are no valid prices follow
      }
      double price = input.nextDouble();
      Item it = new Item(itemName, price); //Creates new item object
      this.itemsDesired.add(it); //Adds to the desired list
    }
    input.close(); //Closes once it's done reading
  }
    
  /*
   * Compares the data in the given Department to the data in this Department
   * Returns -1 if this Department comes first
   * Returns 0 if these Departments have equal priority
   * Returns 1 if the given Department comes first
   *
   * This function is to ensure the departments are sorted by the priority when put in the priority queue 
   */
  //This compairs the priorities (makes the lower in priority earlier in the queue)
  public int compareTo( Department dept ){
    int cmp = this.priority.compareTo( dept.priority );
    if (cmp != 0) return cmp;
    return this.priority.compareTo( dept.priority );
  }

  //Makes the departments equal if the names are matching
  
  public boolean equals( Department dept ){
    return this.name.compareTo( dept.name ) == 0;
  }

  @Override 
  @SuppressWarnings("unchecked") //Suppresses warning for cast
  public boolean equals(Object aThat) {
    if (this == aThat) //Shortcut the future comparisons if the locations in memory are the same
      return true;
    if (!(aThat instanceof Department))
      return false;
    Department that = (Department)aThat;
    return this.equals( that ); //Use above equals method
  }
  
  @Override
  public int hashCode() {
    return name.hashCode(); /* use the hashCode for data stored in this name */
  }

  /* Debugging tool
   * Converts this Department to a string
   */		
  public String toString() {
    return "NAME: " + name + "\nPRIORITY: " + priority + "\nDESIRED: " + itemsDesired + "\nRECEIVED " + itemsReceived + "\nREMOVED " + itemsRemoved + "\n";
  }
}

/* Item
 *
 * Stores the information associated with an Item which is desired by a Department
 */
class Item
{
  String name;    /* name of this item */
  Double price;   /* price of this item */

  /*
   * Constructor to build a Item
   */
  public Item( String name, Double price ){
    this.name = name;
    this.price = price;
  }

  /* Debugging tool
   * Converts this Item to a string
   */		
  public String toString() {
    return "{ " + name + ", " + price + " }";
  }
}
