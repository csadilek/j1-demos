/*
 * Copyright 2011 JBoss, by Red Hat, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.errai.judcon.client.local;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Dependent;

import org.jboss.errai.judcon.client.shared.Customer;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

@Dependent
public class CustomerTable extends Composite {
 
  public static interface NewCustomerCallback {
    void onNewCustomer(Customer customer);
  }

  final private FlexTable customersTable = new FlexTable();
  final private TextBox custFirstName = new TextBox();
  final private TextBox custLastName = new TextBox();
  final private TextBox custPostalCode = new TextBox();

  final Map<Long, Integer> rows = new HashMap<Long, Integer>();

  private NewCustomerCallback newCustomerCallback;

  public CustomerTable() {
    final Button create = new Button("Create", new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        Customer customer = new Customer(custFirstName.getText(), custLastName.getText(), custPostalCode.getText());
        newCustomerCallback.onNewCustomer(customer);
        custFirstName.setText("");
        custLastName.setText("");
        custPostalCode.setText("");
      }
    });

    customersTable.setText(0, 0, "ID");
    customersTable.setText(0, 1, "First Name");
    customersTable.setText(0, 2, "Last Name");
    customersTable.setText(0, 3, "Postal Code");
    customersTable.setText(0, 4, "Date Changed");

    FlexTable newCustomerTable = new FlexTable();
    newCustomerTable.setWidget(0, 1, custFirstName);
    newCustomerTable.setWidget(0, 2, custLastName);
    newCustomerTable.setWidget(0, 3, custPostalCode);
    newCustomerTable.setWidget(0, 4, create);
    newCustomerTable.setStyleName("new-customer-table");

    VerticalPanel vPanel = new VerticalPanel();
    vPanel.add(customersTable);
    vPanel.add(new HTML("<hr>"));
    vPanel.add(newCustomerTable);
    vPanel.addStyleName("whole-customer-table");
    initWidget(vPanel);
  }

  public void addCustomers(final List<Customer> customers) {
    for (Customer customer : customers) {
      addCustomer(customer);
    }
  }

  public void addCustomer(final Customer customer) {
    if (rows.containsKey(customer.getId()))
      return;
    
    int row = customersTable.getRowCount();
    final TextBox firstName = new TextBox();
    firstName.setText(customer.getFirstName());

    final TextBox lastName = new TextBox();
    lastName.setText(customer.getLastName());

    final TextBox postalCode = new TextBox();
    postalCode.setText(customer.getPostalCode());

    customersTable.setText(row, 0, new Long(customer.getId()).toString());
    customersTable.setWidget(row, 1, firstName);
    customersTable.setWidget(row, 2, lastName);
    customersTable.setWidget(row, 3, postalCode);
    customersTable.setText(row, 4, DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").format(customer.getLastChanged()));
    rows.put(customer.getId(), row);
  }

  public void setNewCustomerCallback(NewCustomerCallback newCustomerCallback) {
    this.newCustomerCallback = newCustomerCallback;
  }
}