package extensions.loyalty;

import services.loyalty.award.IAwardProvider;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface IloyaltyExtension extends IExtensionPoint  {
	public void registerAwardFragment(
			Multibinder<IAwardProvider> plugins);

}
