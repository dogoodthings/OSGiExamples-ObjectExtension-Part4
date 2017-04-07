//*****************************************************************************
//
//                            Copyright (c) 2016
//                    DSC Software AG, Karlsruhe, Germany
//                            All rights reserved
//
//        The contents of this file is an unpublished work protected
//        under the copyright law of the Federal Republic of Germany
//
//        This software is proprietary to and emboddies confidential
//        technology of DSC Software AG. Possession, use and copying
//        of the software and media is authorized only pursuant to a
//        valid written license from DSC Software AG. This copyright
//        statement must be visibly included in all copies.
//
//*****************************************************************************

//*****************************************************************************
//
// $Revision: 71390 $ ($Date: 2016-12-22 15:06:59 +0100 (Do, 22 Dez 2016) $)
//
// filename : MaterialExtension.java
//
// contents :
//
// created at : 22.11.2016
// created by : vko, DSC Software AG, Karlsruhe, Germany
//
//*****************************************************************************

package osgi.fieldextension;

import com.dscsag.plm.spi.interfaces.ECTRService;
import com.dscsag.plm.spi.interfaces.objects.ObjectData;
import com.dscsag.plm.spi.interfaces.objects.ObjectExtensionService;
import com.dscsag.plm.spi.interfaces.objects.ObjectFieldExtensionService;
import com.dscsag.plm.spi.interfaces.objects.doc.DocumentDataKeys;
import com.dscsag.plm.spi.interfaces.objects.mat.MaterialDataKeys;
import com.dscsag.plm.spi.interfaces.objects.util.ObjectExtensionHelper;

/**
 * <description>
 *
 * @author vko
 * @version $Revision: 71390 $ ($Date: 2016-12-22 15:06:59 +0100 (Do, 22 Dez 2016) $)
 *
 */
public class ObjectFieldExtension implements ObjectExtensionService
{
  public static final String ID = "@(#) $Id: ObjectFieldExtension.java 71390 2016-12-22 14:06:59Z wt $";

  private ObjectExtensionHelper helper;

  private ECTRService ectrService;

  public ObjectFieldExtension(ObjectExtensionHelper h, ECTRService ectrService)
  {
    helper = h;
    this.ectrService = ectrService;
  }

  @Override
  public ObjectFieldExtensionService getObjectFieldExtensionService(String fieldName, ObjectData data)
  {
    ObjectFieldExtensionService service = null;
    if (fieldName.startsWith(MaterialDataKeys.TABLE_TYPE))
      service = new MaterialFieldExtension();
    else if (fieldName.startsWith(DocumentDataKeys.TABLE_TYPE))
    {
      if (DocumentDataKeys.NUMBER.equals(fieldName))
        service = new DocumentNumberExtension(ectrService);
      else
        service = new DocumentFieldExtension(helper);
    }
    return service;
  }
}
