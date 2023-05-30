import java.awt.print.*;
public class PrintReportSender implements ScoreReportSender {
    @Override
    public void send(String recipient, String content) {
        PrinterJob job = PrinterJob.getPrinterJob();

		PrintableText printobj = new PrintableText(content);

		job.setPrintable(printobj);

		if (job.printDialog()) {
			try {
				job.print();
			} catch (PrinterException e) {
				System.out.println(e);
			}
		}
    }
}
