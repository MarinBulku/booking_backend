package com.algorhythm.booking_backend.application.service.implementations;

import com.algorhythm.booking_backend.adapter.out.persistence.BookingRepository;
import com.algorhythm.booking_backend.adapter.out.persistence.PdfReceiptRepository;
import com.algorhythm.booking_backend.application.service.interfaces.PdfService;
import com.algorhythm.booking_backend.core.entities.Booking;
import com.algorhythm.booking_backend.core.entities.PdfReceipt;
import com.algorhythm.booking_backend.core.exceptions.EntityNotFoundException;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.DashedLine;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {

    private final BookingRepository bookingRepository;
    private final PdfReceiptRepository receiptRepository;

    @Value("${pdf.folder.path}")
    private String folderPath;

    @Override
    public byte[] getRecipeOfBooking(Integer bookingId) throws IOException {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new EntityNotFoundException("No booking with id: " + bookingId));
        Optional<PdfReceipt> receipt = receiptRepository.findByFileName("Booking" + booking.getBookingId() + ".pdf");

        if (receipt.isPresent()){
            File pdfFile = new File(receipt.get().getFilePath());
            return Files.readAllBytes(pdfFile.toPath());
        }

        byte[] generatedPdf = generateDocument(booking);
        PdfReceipt newReceipt = PdfReceipt.builder()
                .booking(booking)
                .fileName("Booking" + booking.getBookingId() + ".pdf")
                .filePath(folderPath + "Booking" + booking.getBookingId() + ".pdf")
                .fileSize((double)generatedPdf.length/1024*1024)
                .build();
        receiptRepository.save(newReceipt);
        return generatedPdf;
    }

    private byte[] generateDocument(Booking booking) throws IOException, SecurityException {

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        File folder = new File(folderPath);

        // Ensure the folder exists
        if (!folder.exists() && !folder.mkdir()) throw new IOException("Cannot create a folder!");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(folderPath + "/Booking" + booking.getBookingId() + ".pdf");
        } catch (Exception e) {
            File f = new File(folderPath + "/Booking" + booking.getBookingId() + ".pdf");
            if (f.createNewFile()) fos = new FileOutputStream(f);
        }

        PdfWriter writer = new PdfWriter(bao);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4, true);

        // Title Section
        Paragraph title = new Paragraph("BOOKING ONLINE")
                .setBold()
                .setItalic()
                .setFontSize(26.5f)
                .setTextAlignment(TextAlignment.CENTER);

        LineSeparator titleSeparator = new LineSeparator(new SolidLine(2f));
        titleSeparator.setWidth(UnitValue.createPercentValue(100));

        // Booking For Section
        Paragraph bookingForSubtitle = new Paragraph("Booking for client: ")
                .setFontSize(20.0f)
                .setTextAlignment(TextAlignment.LEFT)
                .setItalic();

        List bookedForDetails = new List()
                .setSymbolIndent(12)
                .setListSymbol("•")
                .setFontSize(14);

        bookedForDetails.add(new ListItem("Reserved for: " + booking.getReservedFor()));
        bookedForDetails.add(new ListItem("Email : " + booking.getEmail()));
        bookedForDetails.add(new ListItem("Phone number: " + booking.getPhoneNumber()));
        bookedForDetails.add(new ListItem("Address : " + booking.getAddress()));

        LineSeparator bookedForSeparator = new LineSeparator(new DashedLine(1f));
        bookedForSeparator.setWidth(UnitValue.createPercentValue(100));

        // Booked By Section
        Paragraph bookedBySubtitle = new Paragraph("Booked by client: ")
                .setFontSize(20.0f)
                .setTextAlignment(TextAlignment.LEFT);

        List bookedByDetails = new List()
                .setSymbolIndent(12)
                .setListSymbol("•")
                .setFontSize(14)
                .setItalic();

        bookedByDetails.add(new ListItem("Reserved by: " + booking.getUser().getFullName()));
        bookedByDetails.add(new ListItem("Email: " + booking.getUser().getEmail()));
        bookedByDetails.add(new ListItem("Phone number: " + booking.getUser().getPhoneNumber()));
        bookedByDetails.add(new ListItem("Address: " + booking.getUser().getAddress()));

        LineSeparator bookedSeparator = new LineSeparator(new DashedLine(1f));
        bookedSeparator.setWidth(UnitValue.createPercentValue(100));

        // Booking Details Title
        Paragraph bookingDetailsTitle = new Paragraph("Booking information")
                .setFontSize(20.0f)
                .setTextAlignment(TextAlignment.CENTER)
                .setItalic()
                .setMarginTop(10); // Adjust margin for spacing

        // Booking Table
        Table bookingTable = new Table(2, true);
        bookingTable.setBorder(new SolidBorder(ColorConstants.BLACK, 2));

        bookingTable.addHeaderCell(new Cell(1, 2).setTextAlignment(TextAlignment.CENTER).add(new Paragraph("INFORMATION ABOUT TRANSCRIPT")));
        bookingTable.addCell("Hotel:");
        bookingTable.addCell(booking.getRoom().getHotel().getHotelName());
        bookingTable.addCell("Room:");
        bookingTable.addCell(booking.getRoom().getRoomName());
        bookingTable.addCell("Adults:");
        bookingTable.addCell(String.valueOf(booking.getRoom().getAdultsCapacity()));
        bookingTable.addCell("Kids:");
        bookingTable.addCell(String.valueOf(booking.getRoom().getKidsCapacity()));
        bookingTable.addCell("Start date:");
        bookingTable.addCell(booking.getStartDate().toString());
        bookingTable.addCell("End date:");
        bookingTable.addCell(booking.getEndDate().toString());
        bookingTable.addCell("No. of nights:");
        bookingTable.addCell(String.valueOf(DAYS.between(booking.getStartDate(), booking.getEndDate())));
        bookingTable.addCell("Price paid:");
        bookingTable.addCell(String.valueOf(booking.getPricePaid()) + " EUR");
        bookingTable.addFooterCell(new Cell(1, 2).setTextAlignment(TextAlignment.CENTER).add(new Paragraph("")));
        bookingTable.setPaddingBottom(10.0f);

        LineSeparator tableSeparator = new LineSeparator(new DashedLine(1f));
        tableSeparator.setWidth(UnitValue.createPercentValue(100));

        // Hotel and Room Info Subtitle
        Paragraph hotelAndRoomInfoSubtitle = new Paragraph("Information about the hotel and room you will be staying")
                .setFontSize(16.0f)
                .setTextAlignment(TextAlignment.LEFT)
                .setItalic();

        Paragraph roomDescription = new Paragraph("Room Description: " + booking.getRoom().getDescription())
                .setFontSize(14.0f)
                .setTextAlignment(TextAlignment.LEFT);

        // Hotel Facilities Section
        StringBuilder hotelFacilities = new StringBuilder();
        if (booking.getRoom().getHotel().isFreeWiFi()) hotelFacilities.append("free Wi-Fi, ");
        if (booking.getRoom().getHotel().isFreeParking()) hotelFacilities.append("free Parking, ");
        if (booking.getRoom().getHotel().isFreePool()) hotelFacilities.append("free Pool, ");
        if (booking.getRoom().getHotel().isFreeBreakfast()) hotelFacilities.append("free Breakfast, ");

        Paragraph facilities = null;
        if (!hotelFacilities.isEmpty()) {
            hotelFacilities.setLength(hotelFacilities.length() - 2);
            facilities = new Paragraph("Hotel Facilities: " + hotelFacilities)
                    .setFontSize(14.0f)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setItalic();
        }

        Paragraph picIllustrate = new Paragraph("Pictures of the hotel and room you will be staying")
                .setFontSize(14.0f)
                .setTextAlignment(TextAlignment.CENTER);

        Image image = null;
        if (booking.getRoom().getRoomImagePath() != null) {
            try {
                byte[] imageBytes = Files.readAllBytes(Paths.get(booking.getRoom().getRoomImagePath()));
                ImageData imageData = ImageDataFactory.create(imageBytes);
                image = new Image(imageData);
                image.scaleToFit(200, 200);
            } catch (Exception e) {
                throw new IOException(e);
            }
        }

        Image hotelImage = null;
        String hotelImagePath = booking.getRoom().getHotel().getHotelImagePath();
        if (hotelImagePath != null && !hotelImagePath.isEmpty()) {
            ImageData hotelImageData = ImageDataFactory.create(hotelImagePath);
            hotelImage = new Image(hotelImageData);
            hotelImage.scaleToFit(200, 200);
            hotelImage.setTextAlignment(TextAlignment.CENTER);
        }

        Table imageTable = new Table(2);
        imageTable.setWidth(UnitValue.createPercentValue(100));
        imageTable.setBorder(Border.NO_BORDER);
        imageTable.setHorizontalAlignment(HorizontalAlignment.CENTER);

        if (hotelImage != null) {
            Cell hotelImageCell = new Cell().add(hotelImage)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBorder(Border.NO_BORDER);
            imageTable.addCell(hotelImageCell);
        } else {
            imageTable.addCell(new Cell().setBorder(Border.NO_BORDER));
        }

        if (image != null) {
            Cell roomImageCell = new Cell().add(image)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBorder(Border.NO_BORDER);
            imageTable.addCell(roomImageCell);
        } else {
            imageTable.addCell(new Cell().setBorder(Border.NO_BORDER));
        }

        document.add(title);
        document.add(titleSeparator);
        document.add(bookingForSubtitle);
        document.add(bookedForDetails);
        document.add(bookedForSeparator);
        document.add(bookedBySubtitle);
        document.add(bookedByDetails);
        document.add(bookedSeparator);
        document.add(bookingDetailsTitle);
        document.add(bookingTable);
        document.add(new Paragraph().setMarginBottom(5f)); // Adjust spacing between table and separator
        document.add(tableSeparator);
        document.add(new Paragraph().setMarginBottom(10f)); // Adjust spacing after separator
        document.add(hotelAndRoomInfoSubtitle);
        document.add(roomDescription);
        document.add(facilities != null ? facilities : new Paragraph(hotelFacilities.toString()));
        document.add(picIllustrate);
        document.add(imageTable);
        document.close();

        bao.writeTo(fos);
        return bao.toByteArray();
    }

}
