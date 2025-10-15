package com.codeup.novabook.utils;

import com.codeup.novabook.domain.Book;
import com.codeup.novabook.domain.Lending;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for exporting data to CSV files.
 */
public class CsvExporter {
    private static final Logger LOGGER = Logger.getLogger(CsvExporter.class.getName());

    /**
     * Exports the complete book catalog to a CSV file.
     *
     * @param books    List of books to export
     * @param filePath Path where the CSV file will be created
     * @return true if export was successful, false otherwise
     */
    public static boolean exportBooksToCsv(List<Book> books, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write header
            writer.write("ISBN,Title,Author,Stock,CreatedAt,UpdatedAt\n");

            // Write book data
            for (Book book : books) {
                writer.write(String.format("%s,\"%s\",\"%s\",%d,%s,%s\n",
                        book.getIsbn(),
                        escapeCsvField(book.getTitle()),
                        escapeCsvField(book.getAuthor()),
                        book.getStock(),
                        book.getCreatedAt(),
                        book.getUpdatedAt()));
            }

            LOGGER.log(Level.INFO, "Successfully exported {0} books to {1}",
                    new Object[] { books.size(), filePath });
            return true;

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error exporting books to CSV", e);
            return false;
        }
    }

    /**
     * Exports overdue lendings to a CSV file.
     *
     * @param lendings List of overdue lendings to export
     * @param filePath Path where the CSV file will be created
     * @return true if export was successful, false otherwise
     */
    public static boolean exportOverdueLendingsToCsv(List<Lending> lendings, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write header
            writer.write("LendingID,BookTitle,MemberName,LendDate,DueDate,DaysOverdue\n");

            // Write lending data
            for (Lending lending : lendings) {
                writer.write(String.format("%d,%d,%d,%s,%s,%d\n",
                        lending.getId(),
                        lending.getBookId(),
                        lending.getMemberId(),
                        lending.getLendingDate(),
                        lending.getDueDate(),
                        lending.getDaysOverdue()));
            }

            LOGGER.log(Level.INFO, "Successfully exported {0} overdue lendings to {1}",
                    new Object[] { lendings.size(), filePath });
            return true;

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error exporting overdue lendings to CSV", e);
            return false;
        }
    }

    /**
     * Escapes special characters in CSV fields.
     *
     * @param field The field to escape
     * @return The escaped field
     */
    private static String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }
        return field.replace("\"", "\"\"");
    }
}