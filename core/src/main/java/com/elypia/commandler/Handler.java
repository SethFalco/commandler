package com.elypia.commandler;

import com.elypia.commandler.impl.*;
import com.elypia.commandler.metadata.ModuleData;

public abstract class Handler<C, E, M> implements IHandler<C, E, M> {

	protected Commandler<C, E, M> commandler;
	protected ModuleData module;

	protected C client;

	/**
	 * If this module is enabled or out of service.
	 */
	protected boolean enabled;

	/**
	 * Initialise the module, this will assign the values
	 * in the module and create a {@link ModuleData} which is
	 * what {@link Commandler} uses in runtime to identify modules,
	 * commands or obtain any static data.
	 *
	 * @param commandler Our parent Commandler class.
	 * @return Returns if the {@link #test()} for this module passed.
	 */
	@Override
	public boolean init(Commandler<C, E, M> commandler) {
		this.commandler = commandler;
		client = commandler.getClient();
		module = commandler.getContext().getModule(this.getClass());
		return test();
	}

	/**
	 * Performs relevent tests to ensure this module
	 * is still working as intended. Should any test fail we will
	 * still load and display {@link #help(ICommandEvent)} for the module
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

	public void setCommandler(Commandler<C, E, M> commandler) {
		this.commandler = commandler;
	}

	public ModuleData getModule() {
		return module;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public int compareTo(IHandler<C, E, M> o) {
		return module.compareTo(o.getModule());
	}
}
