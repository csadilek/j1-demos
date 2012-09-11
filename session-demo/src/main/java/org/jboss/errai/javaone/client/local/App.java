package org.jboss.errai.javaone.client.local;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.Caller;
import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.errai.javaone.client.local.CustomerTable.NewCustomerCallback;
import org.jboss.errai.javaone.client.shared.Customer;
import org.jboss.errai.javaone.client.shared.CustomerService;

import com.google.gwt.user.client.ui.RootPanel;

@EntryPoint
public class App {

  @Inject
  private Caller<CustomerService> customerService;

  @Inject
  private CustomerTable customerTable;

  @PostConstruct
  void init() {
    RootPanel.get().add(customerTable);
    populateCustomersTable();

    customerTable.setNewCustomerCallback(new NewCustomerCallback() {
      @Override
      public void onNewCustomer(final Customer customer) {
        customerService.call(new RemoteCallback<Long>() {
          @Override
          public void callback(Long response) {
            customer.setId(response);
            customerTable.addCustomer(customer);
          }
        }).createCustomer(customer);
      }
    });
  }

  public void populateCustomersTable() {
    final RemoteCallback<List<Customer>> listCallback = new RemoteCallback<List<Customer>>() {
      @Override
      public void callback(List<Customer> customers) {
        customerTable.addCustomers(customers);
      }
    };
    customerService.call(listCallback).listAllCustomers();
  }
}