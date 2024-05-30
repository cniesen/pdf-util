package com.testautomationguru.utility;

import org.apache.pdfbox.text.PDFTextStripper;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.reporters.Files;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class PDFUtilTest {

    PDFUtil pdfutil = new PDFUtil();

    @Test(priority = 1)
    public void checkForPDFPageCount() throws IOException {
        int actual = pdfutil.getPageCount("src/test/resources/image-extract/sample.pdf");
        Assert.assertEquals(actual, 6);
    }

    @Test(priority = 2)
    public void checkForFileContent() throws IOException {
        String actual = pdfutil.getText("src/test/resources/text-extract/sample.pdf");
        String expected = Files.readFile(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("text-extract/expected.txt")));
        Assert.assertEquals(actual.trim(), expected.trim());
    }

    @Test(priority = 3)
    public void checkForFileContentUsingStripper() throws IOException {
        String actual = pdfutil.getText("src/test/resources/text-extract-position/sample.pdf");
        String expected = Files.readFile(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("text-extract-position/expected.txt")));
        Assert.assertNotEquals(actual.trim(), expected.trim());

        //should match with stripper
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setSortByPosition(true);
        pdfutil.useStripper(stripper);
        actual = pdfutil.getText("src/test/resources/text-extract-position/sample.pdf");
        expected = Files.readFile(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("text-extract-position/expected.txt")));
        Assert.assertEquals(actual.trim(), expected.trim());
        pdfutil.useStripper(null);
    }

    @Test(priority = 4)
    public void extractImages() throws IOException {
        List<String> actualExtractedImages = pdfutil.extractImages("src/test/resources/image-extract/sample.pdf");
        Assert.assertEquals(actualExtractedImages.size(), 7);
    }

    @Test(priority = 5)
    public void saveAsImages() throws IOException {
        List<String> actualExtractedImages = pdfutil.savePdfAsImage("src/test/resources/image-extract/sample.pdf");
        Assert.assertEquals(actualExtractedImages.size(), 6);
    }

    @Test(priority = 6)
    public void comparePDFTextModeDiff() throws IOException {
        pdfutil.setCompareMode(CompareMode.TEXT_MODE);
        boolean result = pdfutil.compare("src/test/resources/text-compare/sample1.pdf", "src/test/resources/text-compare/sample2.pdf");
        Assert.assertFalse(result);
    }

    @Test(priority = 7)
    public void comparePDFTextModeSameAfterExcludePattern() throws IOException {
        pdfutil.setCompareMode(CompareMode.TEXT_MODE);
        pdfutil.excludeText("\\d+");
        // pdfutil.excludeText("1999","1998");
        boolean result = pdfutil.compare("src/test/resources/text-compare/sample1.pdf", "src/test/resources/text-compare/sample2.pdf");
        Assert.assertTrue(result);
    }

    @Test(priority = 8)
    public void comparePDFImageModeSame() throws IOException {
        pdfutil.setCompareMode(CompareMode.VISUAL_MODE);
        boolean result = pdfutil.compare("src/test/resources/image-compare-same/sample1.pdf", "src/test/resources/image-compare-same/sample2.pdf");
        Assert.assertTrue(result);
    }

    @Test(priority = 9)
    public void comparePDFImageModeDiff() throws IOException {
        pdfutil.highlightPdfDifference(true);
        boolean result = pdfutil.compare("src/test/resources/image-compare-diff/sample1.pdf", "src/test/resources/image-compare-diff/sample2.pdf");
        Assert.assertFalse(result);
    }

    @Test(priority = 10)
    public void comparePDFImageModeDiffSpecificPage() throws IOException {
        pdfutil.highlightPdfDifference(true);
        boolean result = pdfutil.compare("src/test/resources/image-compare-diff/sample1.pdf", "src/test/resources/image-compare-diff/sample2.pdf", 3);
        Assert.assertTrue(result);
    }

}
