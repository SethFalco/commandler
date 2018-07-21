package com.elypia.commandler;

import com.elypia.commandler.annotations.Command;
import com.elypia.commandler.annotations.validation.Ignore;
import com.elypia.commandler.impl.*;
import com.elypia.commandler.metadata.MetaModule;

public abstract class Handler<C, E, M> implements IHandler<C, E, M>, Comparable<Handler<C, E, M>> {

	protected Commandler<C, E, M> commandler;
	protected IConfiler<C, E, M> confiler;
	protected MetaModule<C, E, M> module;

	protected C client;

	/**
	 * If this module is enabled or out of service.
	 */

	protected boolean enabled;

	/**
	 * Initialise the module, this will assign the values
	 * in the module and create a {@link MetaModule} which is
	 * what {@link Commandler} uses in runtime to identify modules,
	 * commands or obtain any static data.
	 *
	 * @param commandler Our parent Commandler class.
	 * @return Returns if the {@link #test()} for this module passed.
	 */

	@Override
	public boolean init(Commandler<C, E, M> commandler) {
		this.commandler = commandler;
		confiler = commandler.getConfiler();
		client = commandler.getClient();
		module = new MetaModule<>(commandler, this);
		return test();
	}

	/**
	 * Performs relevent tests to ensure this module
	 * is still working as intended. Should any test fail we will
	 * still load and display {@link #help(CommandEvent)} for the module
	 * however all other commands will not be possible.
	 *
	 * @return If the module should remain enabled.
	 */

	@Override
	public boolean test() {
		return true;
	}

	public C getClient() {
		return client;
	}

	public Commandler<C, E, M> getCommandler() {
		return commandler;
	}

	public IConfiler<C, E, M> getConfiler() {
		return confiler;
	}

	public MetaModule getModule() {
		return module;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public int compareTo(Handler<C, E, M> o) {
		return module.compareTo(o.module);
	}
}
