/*
 * Copyright 2019-2020 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.commandler.producers;

import org.elypia.commandler.api.Integration;
import org.elypia.commandler.event.*;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

@RequestScoped
public class RequestFactory {

    private Request<?, ?> request;
    private ActionEvent<?, ?> event;
    private Object[] params;

    @RequestScoped
    @Produces
    public Integration getIntegration() {
        return request.getIntegration();
    }

    @RequestScoped
    @Produces
    public Request getRequest() {
        return request;
    }

    public void setRequest(Request<?, ?> request) {
        this.request = request;
    }

    @RequestScoped
    @Produces
    public ActionEvent getEvent() {
        return event;
    }

    public void setEvent(ActionEvent<?, ?> event) {
        this.event = event;
    }

    @RequestScoped
    @Produces
    public ParameterWrapper getParameterWrapper() {
        return new ParameterWrapper(params);
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
