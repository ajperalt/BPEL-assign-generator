package pl.eiti.bpelag.util;

import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.Assign;
import org.eclipse.bpel.model.Empty;
import org.eclipse.bpel.model.Exit;
import org.eclipse.bpel.model.ExtensionActivity;
import org.eclipse.bpel.model.Invoke;
import org.eclipse.bpel.model.Receive;
import org.eclipse.bpel.model.Reply;
import org.eclipse.bpel.model.Rethrow;
import org.eclipse.bpel.model.Throw;
import org.eclipse.bpel.model.Validate;
import org.eclipse.bpel.model.Wait;

public class ActivityUtil {
	private static final Class<?>[] BASIC_ACTIVITIES = { Assign.class, Empty.class, Exit.class,
			ExtensionActivity.class, Invoke.class, Receive.class, Reply.class, Rethrow.class, Throw.class,
			Validate.class, Wait.class };

	public static Boolean isBasicActivity(Activity act) {
		Boolean isBasic = Boolean.FALSE;
		for (Class<?> classType : BASIC_ACTIVITIES) {
			if (classType.isInstance(act)) {
				isBasic = Boolean.TRUE;
			}
		}
		return isBasic;
	}

}