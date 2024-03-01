import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

class Books {
     // ALL THE QUERIES ARE HERE CALLED ALL OVER THE FUNCTION
     // QUERY TO ADD BOOK IN BOOKS TABLE
     final String addBook = "INSERT INTO books (book_name,author) VALUES ( ?,?)";
     // QUERY TO ADD BOOK AND ISSUAR DETAILS IN ISSUANCE TABLE
     final String addIssuer = "INSERT INTO issuance (book_id, issuer_name, issuer_contact_details, issued_date) "
               + "SELECT ?, ?, ?, NOW() "
               + "FROM books "
               + "WHERE book_id = ?";
     // QUERY TO GET ISSUED ID
     final String retrieveIssuedId = "SELECT LAST_INSERT_ID()";
     // QUERY TO FIND THE STATUS OF THE BOOK(AVIALBLE OR ISSUED)
     private final String getBookStatusQuery = "SELECT status FROM books WHERE book_id = ?";
     // DISPLAING ALL THE BOOKS
     String select = "SELECT * FROM books";
     // QUERY TO UPDATE BOOK STATUS IN BOOKS TABLE AS ISSUED
     final String updateBookStatus = "UPDATE books SET status = 'issued' WHERE book_id = ?";
     // QUERY TO UPDATE STAUTS IN BOOKS TABLE AS AVAILABLE
     final String updateBooksStatusQuery = "UPDATE books SET status = 'Available' WHERE book_id = ?";
     // STATUS AS RETURNED IN ISSUANCE TABLE
     final String updateIssuanceStatusQuery = "UPDATE issuance SET status = 'returned' WHERE issue_id = ?";
     // GETTING BOOK_ID FROM ISSUANCE TABLE
     private final String getBookIdQuery = "SELECT book_id FROM issuance WHERE issue_id = ?";

     // METHOD TO ADD BOOKS IN THE LIST/TABLE.
     public void addBook(Connection con, Scanner sc) {
          System.out.println("--------------- ADDING NEW BOOKS -------------");
          System.out.println("How many books do you want to add? ");
          int count = sc.nextInt();
          sc.nextLine();
          while (count != 0) {
               System.out.println("Enter the name of Book:-");
               String name = sc.nextLine();
               System.out.println("Enter the Author of the Book: ");
               String author = sc.nextLine();
               try (PreparedStatement ps = con.prepareStatement(addBook)) {
                    ps.setString(1, name);
                    ps.setString(2, author);
                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected > 0) {
                         System.out.println("Book inserted Successfully!");
                    } else {
                         System.out.println("Error while inserting book!");
                    }
               } catch (Exception e) {
                    System.out.println("Error while adding book " + e.getMessage());
               }
               count--;
          }
     }

     public void issueBook(Connection con, Scanner sc) {
          System.out.println("------------ ISSUING BOOK ----------");
          System.out.println("Enter the book ID: ");
          int bookId = sc.nextInt();
          sc.nextLine(); // Consume newline
          try (PreparedStatement issueBookPs = con.prepareStatement(addIssuer);
                    PreparedStatement updateBooksStatusPs = con.prepareStatement(updateBookStatus);
                    PreparedStatement getBookStatusPs = con.prepareStatement(getBookStatusQuery)) {
               // Check if the book is available
               getBookStatusPs.setInt(1, bookId);
               ResultSet rs = getBookStatusPs.executeQuery();
               if (rs.next()) {
                    String status = rs.getString("status");
                    if ("available".equals(status)) {
                         // Book is available, issue it
                         System.out.println("Enter customer name: ");
                         String customerName = sc.nextLine();
                         System.out.println("Enter customer phone number: ");
                         String phoneNumber = sc.nextLine();
                         // Insert record into issuance table
                         issueBookPs.setInt(1, bookId);
                         issueBookPs.setString(2, customerName);
                         issueBookPs.setString(3, phoneNumber);
                         issueBookPs.setInt(4, bookId);
                         int rowsAffected = issueBookPs.executeUpdate();
                         if (rowsAffected > 0) {
                              // Update status to 'issued' in the books table
                              updateBooksStatusPs.setInt(1, bookId);
                              updateBooksStatusPs.executeUpdate();
                              System.out.println("Book has been issued successfully!");
                         } else {
                              System.out.println("Failed to issue the book.");
                         }
                    } else {
                         // Book is not available
                         System.out.println("Sorry, the book is not available.");
                    }
               } else {
                    // Book ID not found
                    System.out.println("Invalid book ID.");
               }
          } catch (SQLException e) {
               System.out.println("Error while issuing book: " + e.getMessage());
          }
     }

     public void returnBook(Connection con, Scanner sc) {
          System.out.println("---------------- RETURNING BOOK ---------------");
          System.out.println("Enter issued id: ");
          int issueId = sc.nextInt();
          try (PreparedStatement returnBookPs = con.prepareStatement(updateIssuanceStatusQuery);
                    PreparedStatement updateBooksStatusPs = con.prepareStatement(updateBooksStatusQuery);
                    PreparedStatement getBookIdPs = con.prepareStatement(getBookIdQuery)) {
               // Set issue_id parameter for the returnBook query
               returnBookPs.setInt(1, issueId);
               int rowsAffected = returnBookPs.executeUpdate();
               if (rowsAffected > 0) {
                    // Get the book ID associated with the issued ID
                    getBookIdPs.setInt(1, issueId);
                    ResultSet rs = getBookIdPs.executeQuery();
                    if (rs.next()) {
                         int bookId = rs.getInt("book_id");
                         // Update status to 'available' in the books table
                         updateBooksStatusPs.setInt(1, bookId);
                         updateBooksStatusPs.executeUpdate();
                         System.out.println("Book returned successfully!");
                    }
               } else {
                    System.out.println("Failed to return the book!");
               }
          } catch (SQLException e) {
               System.out.println("Error while returning book: " + e.getMessage());
          }
     }

     public void availableBooks(Connection con, Scanner sc) {
          try (PreparedStatement ps = con.prepareStatement(select);
                    ResultSet rs = ps.executeQuery()) {
               System.out.println(
                         "+--------------+------------------------+---------------------------+------------------+");
               System.out.printf("| %-12s | %-24s | %-27s | %-15s |%n", "Book ID", "Book Name", "Author", "Status");
               System.out.println(
                         "+--------------+------------------------+---------------------------+------------------+");
               while (rs.next()) {
                    int bookId = rs.getInt("book_id");
                    String bookName = rs.getString("book_name");
                    String author = rs.getString("author");
                    String status = rs.getString("status"); // Assuming the name of the status column is "status"
                    System.out.printf("| %-12d | %-24s | %-27s | %-15s |%n", bookId, bookName, author, status);
               }
               System.out.println(
                         "+--------------+------------------------+---------------------------+------------------+");
          } catch (SQLException e) {
               System.out.println("Error while fetching books: " + e.getMessage());
          }
     }

}

public class Library {

     public static void Animation(int delay, int rotation) {
          String[] frames = { "\\", "|", "/", "-" };
          // int delay = 100; // milliseconds
          for (int i = 0; i < 6; i++) {
               for (String frame : frames) {
                    System.out.print("\r" + frame + " LOADING...PLEASE WAIT!.... ");
                    try {
                         Thread.sleep(delay);
                    } catch (InterruptedException e) {
                         e.printStackTrace();
                    }
               }
          }
          System.out.println();
     }

     public static void main(String[] args) {
          final String url = "jdbc:mysql://localhost:3306/demo";
          final String userName = "root";
          final String password = "Admin@123";
          Books l = new Books();
          try (Connection con = DriverManager.getConnection(url, userName, password)) {
               System.out.println("--------------WELCOME TO THE LIBRARY--------------");
               int choice;
               boolean validChoice = false;
               Scanner sc = new Scanner(System.in);
               do {
                    try {
                         System.out.println("Enter your choice:");
                         System.out.println("1. Add Book");
                         System.out.println("2. Issue a Book");
                         System.out.println("3. Return a Book");
                         System.out.println("4. Show Available Books");
                         System.out.println("5. EXIT");
                         choice = sc.nextInt();
                         sc.nextLine(); // Consume newline
                         switch (choice) {
                              case 1:
                                   Animation(100, 10);
                                   l.addBook(con, sc);
                                   break;
                              case 2:
                                   Animation(90, 4);
                                   l.issueBook(con, sc);
                                   break;
                              case 3:
                                   Animation(50, 4);
                                   l.returnBook(con, sc);
                                   break;
                              case 4:
                                   Animation(60, 4);
                                   l.availableBooks(con, sc);
                                   break;
                              case 5:
                                   Animation(80, 10);
                                   System.out.println("\rExiting... Done!");
                                   System.out.println("------------- Thanks For Visiting! -----------");
                                   validChoice = true;
                                   break;
                              default:
                                   System.out.println("Enter a valid choice.");
                                   break;
                         }
                    } catch (InputMismatchException e) {
                         System.out.println("Invalid input. Please enter a number.");
                         sc.nextLine(); // Consume the invalid input
                    }
               } while (!validChoice);
               sc.close();
          } catch (SQLException e) {
               System.out.println("Error : " + e.getMessage());
          }
     }
}
