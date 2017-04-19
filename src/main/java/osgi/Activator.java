package osgi;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.dscsag.plm.spi.interfaces.ECTRService;
import com.dscsag.plm.spi.interfaces.objects.ObjectExtensionService;
import com.dscsag.plm.spi.interfaces.objects.util.ObjectExtensionHelper;

import osgi.fieldextension.ObjectFieldExtension;

/**
 * Activator to register provided services
 */
public class Activator implements BundleActivator
{

  @Override
  public void start(BundleContext context) throws Exception
  {
    // first, get ECTRService which provides some common useful services like
    // logging,
    // dictionary and so on.
    ECTRService ectrService = getService(context, ECTRService.class);

    ObjectExtensionHelper helper = getService(context, ObjectExtensionHelper.class);
    context.registerService(ObjectExtensionService.class, new ObjectFieldExtension(helper, ectrService), null);
  }

  @Override
  public void stop(BundleContext context) throws Exception
  {
    // empty
  }

  private <T> T getService(BundleContext context, Class<T> clazz) throws Exception
  {
    ServiceReference<T> serviceRef = context.getServiceReference(clazz);
    if (serviceRef != null)
      return context.getService(serviceRef);
    throw new Exception("Unable to find implementation for service " + clazz.getName());
  }

}