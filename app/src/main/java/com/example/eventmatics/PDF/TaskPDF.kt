package com.example.eventmatics.PDF
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.ColumnText
import com.itextpdf.text.pdf.PdfContentByte
import com.itextpdf.text.pdf.PdfPageEventHelper
import com.itextpdf.text.pdf.PdfWriter
import java.util.Calendar

class TaskPDF : PdfPageEventHelper() {
    private val headerFont: Font = FontFactory.getFont(FontFactory.HELVETICA, 12f, Font.BOLD)
    private val footerFont: Font = FontFactory.getFont(FontFactory.HELVETICA, 10f, Font.ITALIC)

    override fun onStartPage(writer: PdfWriter?, document: Document?) {
        val pdfContent: PdfContentByte = writer!!.directContent

        // Random Text
        val randomText = "This is a PDF Report For All Task "
        val randomTextFont: Font = FontFactory.getFont(FontFactory.HELVETICA, 10f, Font.NORMAL, BaseColor.GRAY)
        val randomTextPhrase = Phrase(randomText, randomTextFont)
        val randomTextWidth = randomTextFont.getBaseFont().getWidthPoint(randomText, randomTextFont.size)
        val randomTextX = (document!!.pageSize.width - randomTextWidth) / 2
        val randomTextY = document.topMargin() + 50f
        ColumnText.showTextAligned(pdfContent, Element.ALIGN_LEFT, randomTextPhrase, randomTextX, randomTextY, 0f)

        // Date and Time
        val dateText = "Date: ${getCurrentDate()}      Time: ${getCurrentTime()}"
        val dateTimeFont: Font = FontFactory.getFont(FontFactory.HELVETICA, 10f, Font.NORMAL)
        val dateTimePhrase = Phrase(dateText, dateTimeFont)
        val dateTimeWidth = dateTimeFont.getBaseFont().getWidthPoint(dateText, dateTimeFont.size)
        val dateTimeX = (document.pageSize.width - dateTimeWidth) / 2
        val dateTimeY = document.topMargin() + 30f
        ColumnText.showTextAligned(pdfContent, Element.ALIGN_LEFT, dateTimePhrase, dateTimeX, dateTimeY, 0f)
    }
    override fun onEndPage(writer: PdfWriter?, document: Document?) {
        val pdfContent: PdfContentByte = writer!!.directContent


        val headerText = "Task Details"
        val header = Phrase(headerText, headerFont)
        val headerWidth = headerFont.getBaseFont().getWidthPoint(headerText, headerFont.size)
        val headerX = (document!!.pageSize.width - headerWidth) / 2
        val headerY = document.topMargin() + 10f
        ColumnText.showTextAligned(pdfContent, Element.ALIGN_LEFT, header, headerX, headerY, 0f)

        // Footer
        val footerText = "Date: ${getCurrentDate()}      Time: ${getCurrentTime()}"
        val footer = Phrase(footerText, footerFont)
        val footerWidth = footerFont.getBaseFont().getWidthPoint(footerText, footerFont.size)
        val footerX = (document.pageSize.width - footerWidth) / 2
        val footerY = document.bottomMargin() - 20f
        ColumnText.showTextAligned(pdfContent, Element.ALIGN_LEFT, footer, footerX, footerY, 0f)
    }

    private fun getCurrentDate(): String {
        val today = Calendar.getInstance()
        return "${today.get(Calendar.YEAR)}/${today.get(Calendar.MONTH) + 1}/${today.get(Calendar.DAY_OF_MONTH)}"
    }

    private fun getCurrentTime(): String {
        val today = Calendar.getInstance()
        return String.format("%02d:%02d:%02d",
            today.get(Calendar.HOUR_OF_DAY), today.get(Calendar.MINUTE), today.get(Calendar.SECOND))
    }
}
