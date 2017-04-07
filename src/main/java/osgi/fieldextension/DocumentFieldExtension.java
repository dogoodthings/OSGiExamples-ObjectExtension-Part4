package osgi.fieldextension;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.dscsag.plm.spi.interfaces.gui.UIContext;
import com.dscsag.plm.spi.interfaces.objects.ObjectData;
import com.dscsag.plm.spi.interfaces.objects.ObjectFieldExtensionService;
import com.dscsag.plm.spi.interfaces.objects.doc.DocumentDataKeys;
import com.dscsag.plm.spi.interfaces.objects.util.ObjectExtensionHelper;
import com.dscsag.plm.spi.interfaces.objects.util.extensionin.ExtensionInHelper;
import com.dscsag.plm.spi.interfaces.objects.util.extensionin.ExtensionInItem;

class DocumentFieldExtension implements ObjectFieldExtensionService
{
  private ObjectExtensionHelper helper;

  DocumentFieldExtension(ObjectExtensionHelper h)
  {
    helper = h;
  }

  @Override
  public void entryRequested(ObjectData data, UIContext uiContext) throws Exception
  {
    data.objectData().put(DocumentDataKeys.LABOR, "002");
    data.objectData().put(DocumentDataKeys.USER, "Chuck Norris");
    data.objectData().put(DocumentDataKeys.DESCRIPTION, "OSGi");
    data.objectData().put(DocumentDataKeys.LONG_TEXT + "[EN]", "OSGi");

    String extensionData = generateExtensionInData();
    data.objectData().put(DocumentDataKeys.EXTENSIONIN, extensionData);
  }

  @Override
  public void validate(ObjectData data, UIContext uiContext) throws Exception
  {
    // empty
  }

  private String generateExtensionInData()
  {
    ExtensionInHelper extensionInHelper = helper.getExtensionInHelper();
    List<ExtensionInItem> items = IntStream.rangeClosed(1, 10)
        .mapToObj(i -> extensionInHelper.createItem("structure_" + i, "value1_" + i, "value2_" + i, "value3_" + i, "value4_" + i))
        .collect(Collectors.toList());
    return extensionInHelper.serialize(items);
  }
}
