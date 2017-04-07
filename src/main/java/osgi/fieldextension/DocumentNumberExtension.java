package osgi.fieldextension;

import java.util.regex.Pattern;

import com.dscsag.plm.spi.interfaces.ECTRService;
import com.dscsag.plm.spi.interfaces.gui.PlmStatusLineMode;
import com.dscsag.plm.spi.interfaces.gui.UIContext;
import com.dscsag.plm.spi.interfaces.objects.ObjectData;
import com.dscsag.plm.spi.interfaces.objects.ObjectFieldExtensionService;
import com.dscsag.plm.spi.interfaces.objects.doc.DocumentDataKeys;
import com.dscsag.plm.spi.interfaces.rfc.RfcResult;
import com.dscsag.plm.spi.interfaces.rfc.RfcStructure;
import com.dscsag.plm.spi.rfc.builder.RfcCallBuilder;

public class DocumentNumberExtension implements ObjectFieldExtensionService
{
  private ECTRService ectrService;

  public DocumentNumberExtension(ECTRService ectrService)
  {
    this.ectrService = ectrService;
  }

  @Override
  public void entryRequested(ObjectData data, UIContext uiContext) throws Exception
  {
    String documentNumber = data.objectData().get(DocumentDataKeys.NUMBER);
    String documentType = data.objectData().get(DocumentDataKeys.TYPE);
    String documentVersion = data.objectData().get(DocumentDataKeys.VERSION);
    String documentPart = data.objectData().get(DocumentDataKeys.PART);
    data.objectData().put(DocumentDataKeys.NUMBER, replaceStuff(documentNumber, documentType, documentVersion, documentPart));
  }

  @Override
  public void validate(ObjectData data, UIContext uiContext) throws Exception
  {
    // ---
  }

  private String getNextDocumentNumber(String documentType)
  {
    String documentNumber = null;
    RfcCallBuilder b = new RfcCallBuilder("/DSCSAG/DOC_NUMBER_NEXT_GET");
    b.setInputParameter("IV_DOKAR", documentType);
    RfcResult rfcResult = ectrService.getRfcExecutor().execute(b.toRfcCall());

    RfcStructure esReturn = rfcResult.getExportParameter("ES_RETURN").getStructure();
    if ("E".equalsIgnoreCase(esReturn.getFieldValue("TYPE")) || "A".equalsIgnoreCase(esReturn.getFieldValue("TYPE")))
      ectrService.getStatusLine().setText(PlmStatusLineMode.ERROR,
          "Error fetching next document number: " + esReturn.getFieldValue("MESSAGE"));
    else
      documentNumber = rfcResult.getExportParameter("EV_DOKNR").getString();

    if (documentNumber != null)
      documentNumber = documentNumber.replaceAll("^0*([1-9].*)", "$1");

    return documentNumber;
  }

  private String replaceStuff(String documentNumberTemplate, String documentType, String documentVersion, String documentPart)
  {
    String DOCNO = "${DRAW%DOCNO}";
    String DOCTYPE = "${DRAW%DOCTYPE}";
    String DOCVERS = "${DRAW%DOCVERS}";
    String DOCPART = "${DRAW%DOCPART}";
    String expanded = documentNumberTemplate;

    if (expanded.indexOf(DOCNO) != -1)
    {
      String documentNumber = getNextDocumentNumber(documentType);
      if (documentNumber != null && !"".equals(documentNumber))
        expanded = expanded.replaceAll(Pattern.quote(DOCNO), documentNumber);
    }

    expanded = expanded.replaceAll(Pattern.quote(DOCTYPE), documentType);
    expanded = expanded.replaceAll(Pattern.quote(DOCVERS), documentVersion);
    expanded = expanded.replaceAll(Pattern.quote(DOCPART), documentPart);
    return expanded;
  }

}