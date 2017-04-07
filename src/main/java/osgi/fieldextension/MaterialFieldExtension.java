package osgi.fieldextension;

import com.dscsag.plm.spi.interfaces.gui.UIContext;
import com.dscsag.plm.spi.interfaces.objects.ObjectData;
import com.dscsag.plm.spi.interfaces.objects.ObjectFieldExtensionService;
import com.dscsag.plm.spi.interfaces.objects.mat.MaterialDataKeys;

class MaterialFieldExtension implements ObjectFieldExtensionService
{
  @Override
  public void entryRequested(ObjectData data, UIContext uiContext) throws Exception
  {
    data.objectData().entrySet().stream().filter(entry -> entry.getKey().startsWith(MaterialDataKeys.DESCRIPTION))
        .forEach(entry -> data.objectData().put(entry.getKey(), "ABC"));
  }

  @Override
  public void validate(ObjectData data, UIContext uiContext) throws Exception
  {
    // TODO Auto-generated method stub

  }
}
