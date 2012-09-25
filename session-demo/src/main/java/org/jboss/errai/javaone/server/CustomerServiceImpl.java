package org.jboss.errai.javaone.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.errai.javaone.client.shared.Customer;
import org.jboss.errai.javaone.client.shared.CustomerService;

@ApplicationScoped
public class CustomerServiceImpl implements CustomerService {

  private static AtomicLong id = new AtomicLong();
  private static Map<Long, Customer> customers = new ConcurrentHashMap<Long, Customer>() {
    {
      put(id.incrementAndGet(), new Customer(id.get(), "Mike", "Brock", "A1B2C3"));
      put(id.incrementAndGet(), new Customer(id.get(), "Christian", "Sadilek", "A1B2C3"));
      put(id.incrementAndGet(), new Customer(id.get(), "Jonathan", "Fuerth", "A1B2C3"));
      put(id.incrementAndGet(), new Customer(id.get(), "Lincoln", "Baxter", "12345"));
    }
  };

  @Override
  public long createCustomer(Customer customer) {
    customer.setId(id.incrementAndGet());
    customers.put(customer.getId(), customer);
    return customer.getId();
  }

  @Override
  public Customer updateCustomer(long id, Customer customer) {
    customers.put(id, customer);
    customer.setLastChanged(new Date());
    return customer;
  }

  @Override
  public void deleteCustomer(long id) {
    customers.remove(id);
  }

  @Override
  public Customer retrieveCustomerById(long id) {
    return customers.get(id);
  }

  @Override
  public List<Customer> listAllCustomers() {
    List<Customer> customers = new ArrayList<Customer>(CustomerServiceImpl.customers.values());
    Collections.sort(customers);
    return customers;
  }
}