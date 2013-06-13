package front_controllers;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import helpers.HttpHelper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Account;
import models.BankBranch;
import models.Customer;
import models.MapAccountCustomer;

import org.omg.CORBA.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import exceptions.NotFoundException;

import business_controllers.AccountController;
import business_controllers.BankBranchController;
import business_controllers.CustomerController;
import business_controllers.TransactionController;

import utils.ChangesStatus;
import utils.Message;
import utils.MessageQueue;
import utils.PageEnvironment;

@Controller
public class AdminController{
	
	private @Autowired HttpServletRequest request;
	
	@RequestMapping(value = {"/admin" , "/admin/*","/admin/*/*","/admin/*/*/*"})
	public String dashboard(HttpServletRequest request, HttpServletResponse response,
			Model model, final RedirectAttributes redirectAttributes
			) {
		
		PageEnvironment page = new PageEnvironment();
		MessageQueue message_queue = new MessageQueue();
		
		HashMap<String, String> uris = HttpHelper.analyseRequest(request);
		
		//Customer
		if(uris.get("uri_2").equals("customer")){
			CustomerController customerController = new CustomerController();
			//Listing Down the customer
			if(uris.get("uri_3").equals("list")){
				List<Customer> customerList = customerController.getCustomers();
				
				model.addAttribute("data", customerList);
				page.setPage_title("List Customer");
				page.setViewFile("admin/customer_list.jsp");
			}
			//New Customer
			if(uris.get("uri_3").equals("new")){
				Customer customer = new Customer();
				model.addAttribute("data", customer);
				
				page.setPage_title("New Customer");
				page.setViewFile("admin/customer_edit.jsp");
			}
			//Editing a new customer
			if(uris.get("uri_3").equals("edit")){
				try {
					int customer_id = Integer.parseInt(uris.get("uri_4"));
					Customer data = customerController.getCustomer(customer_id);
					model.addAttribute("data", data);
					page.setPage_title("Edit Customer : " + data.getGivenname());
					page.setViewFile("admin/customer_edit.jsp");
				} catch (NotFoundException e) {
					e.printStackTrace();
					message_queue.add(new Message("Customer Not Found", "error"));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					return "redirect:/admin/customer/list";
				} catch (SQLException e) {
					e.printStackTrace();
					message_queue.add(new Message("Sorry, Database has some problem. Try again soon.", "error"));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					return "redirect:/admin/customer/list";
				}
			}
			//Customer search page
			if(uris.get("uri_3").equals("search")){
				page.setPage_title("Search Customer");
				page.setViewFile("admin/customer_search.jsp");
			}
			//Showing customer and his|her Acounts
			if(uris.get("uri_3").equals("details")){
				try {
					int customer_id = Integer.parseInt(uris.get("uri_4"));
					Customer data = customerController.getCustomer(customer_id);
					
					model.addAttribute("data", data);
					page.setPage_title("Customer Detials : " + data.getGivenname());
					page.setViewFile("admin/customer_details.jsp");
				} catch (NotFoundException e) {
					e.printStackTrace();
					message_queue.add(new Message("Customer Not Found", "error"));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					return "redirect:/admin/customer/search";
				} catch (SQLException e) {
					e.printStackTrace();
					message_queue.add(new Message("Sorry, Database has some problem. Try again soon.", "error"));
					redirectAttributes.addFlashAttribute("message_queue", message_queue);
					return "redirect:/admin/customer/search";
				}
			}
		}
		//Account
		if(uris.get("uri_2").equals("account")){
			AccountController accountController = new AccountController();
			if(uris.get("uri_3").equals("list")){
				
			}
			if(uris.get("uri_3").equals("new")){
				Account account = new Account();
				model.addAttribute("data", account);
				
				page.setPage_title("New Account");
				page.setViewFile("admin/account_edit.jsp");
			}
			if(uris.get("uri_3").equals("edit")){
				int account_id = Integer.parseInt(uris.get("uri_4"));
				Account account = accountController.getAccountDetails(account_id);
				model.addAttribute("data", account);
				
				page.setPage_title("Edit Account");
				page.setViewFile("admin/account_edit.jsp");
			}
			//Account search page
			if(uris.get("uri_3").equals("search")){
				page.setPage_title("Search Account");
				page.setViewFile("admin/account_search.jsp");
			}
			//Account search page
			if(uris.get("uri_3").equals("details")){
				int account_id = Integer.parseInt(uris.get("uri_4"));
				Account data = accountController.getAccountDetails(account_id);
				
				System.out.println(data);
				
				model.addAttribute("data", data);
				page.setPage_title("Account Details");
				page.setViewFile("admin/account_details.jsp");
			}
		}
		if(uris.get("uri_2").equals("bank_branch")){
			BankBranchController bankBranchController = new BankBranchController();
			if(uris.get("uri_3").equals("list")){
				List<BankBranch> bankBranchList = bankBranchController.getAllBankBranches();
				model.addAttribute("data", bankBranchList);
				
				page.setPage_title("Bank Branches");
				page.setViewFile("admin/bank_branch_list.jsp");
			}
			if(uris.get("uri_3").equals("new")){
				BankBranch bankBranch = new BankBranch();
				model.addAttribute("data", bankBranch);
				
				page.setPage_title("New Bank Branch");
				page.setViewFile("admin/bank_branch_edit.jsp");
			}
			if(uris.get("uri_3").equals("edit")){
				int bank_branch_id = Integer.parseInt(uris.get("uri_4"));
				BankBranch bankBranch = bankBranchController.getBankBranch(bank_branch_id);
				model.addAttribute("data", bankBranch);
				
				page.setPage_title("Edit Bank Branch");
				page.setViewFile("admin/bank_branch_edit.jsp");
			}
		}
		if(uris.get("uri_2").equals("dashboard") || uris.get("uri_2").equals("")){
			page.setPage_title("Dashboard");
			page.setViewFile("admin/dashboard.jsp");
		}
		
		
		Map<String, MessageQueue> inputFlashMap = (Map<String, MessageQueue>) RequestContextUtils.getInputFlashMap(request);
			if (inputFlashMap != null) {
		    MessageQueue mq =  inputFlashMap.get("message_queue");
		    for (Message m : mq){
		    	message_queue.add(m);
		    }
		}
		
		model.addAttribute("message_queue", message_queue);
		model.addAttribute("page", page);
		return "admin_template";
	}
	
	
	
	@RequestMapping(value = "/admin/post/{to_do}", method = RequestMethod.POST)
	public String customer_list(HttpServletRequest request, HttpServletResponse response
			,Model model, final RedirectAttributes redirectAttributes
			,@PathVariable String to_do
			){
		
		MessageQueue message_queue = new MessageQueue();
		
		if(to_do.equals("save_customer")){
			CustomerController customerController = new CustomerController();
			Customer customer = HttpHelper.sanatizeRequestedCustomerModel(request);
			System.out.print(request.getParameter("customer_id"));
			System.out.println(customer);
			ChangesStatus status = customerController.saveCustomer(customer);
			message_queue.add(new Message(status.getMessage(),((status.getStatus()) ? "success" : "error")));
			redirectAttributes.addFlashAttribute("message_queue", message_queue);
			
			return "redirect:/admin/customer/list";
		}
		
		if(to_do.equals("save_account")){
			AccountController accountController = new AccountController();
			Account account = HttpHelper.sanatizeRequestedAccountModel(request);
			ArrayList<MapAccountCustomer> mapAccountCustomer = HttpHelper.sanatizeRequestedMapAccountCustomerModel(request);
			
			ChangesStatus status = accountController.saveAccount(account, mapAccountCustomer);
			message_queue.add(new Message(status.getMessage(),((status.getStatus()) ? "success" : "error")));
			redirectAttributes.addFlashAttribute("message_queue", message_queue);
			
			return "redirect:/admin/dashboard";
		}
		
		if(to_do.equals("search_customer")){
			return "redirect:/admin/customer/details/" + request.getParameter("customer_id");
		}
		
		if(to_do.equals("search_account")){
			return "redirect:/admin/account/edit/" + request.getParameter("account_id");
		}
		
		if(to_do.equals("save_bank_branch")){
			BankBranchController bankBranchController = new BankBranchController();
			BankBranch bankBranch = HttpHelper.sanatizeRequestedBankBranchModel(request);
			ChangesStatus status = bankBranchController.saveBankBranch(bankBranch);
			message_queue.add(new Message(status.getMessage(),((status.getStatus()) ? "success" : "error")));
			redirectAttributes.addFlashAttribute("message_queue", message_queue);
			
			return "redirect:/admin/bank_branch/list";
		}
		
		
		return "";
	}
	
	/*@RequestMapping("/admin/customer/list")
	public String customer_list(HttpServletRequest request, HttpServletResponse response,
			Model model, final RedirectAttributes redirectAttributes){
		
		PageEnvironment page = new PageEnvironment();
		MessageQueue message_queue = new MessageQueue();
		
		
		return adminTemplate();
	}
	
	@RequestMapping("/admin/customer/new")
	public String customer_new(HttpServletRequest request, HttpServletResponse response,
			Model model, final RedirectAttributes redirectAttributes){
		
		PageEnvironment page = new PageEnvironment();
		MessageQueue message_queue = new MessageQueue();
		page.setView_file("admin/customer_edit");
		
		
		model.addAttribute("message_queue", message_queue);
		model.addAttribute("page", page);
		return adminTemplate();
	}
	
	@RequestMapping("/admin/customer/edit/{id}")
	public String customer_edit(HttpServletRequest request, HttpServletResponse response,
			Model model, final RedirectAttributes redirectAttributes){
		
		
		return adminTemplate();
	}
	
	@RequestMapping("/admin/customer/suspend/{id}")
	public String customer_deactivate(HttpServletRequest request, HttpServletResponse response,
			Model model, final RedirectAttributes redirectAttributes){
		
		return adminTemplate();
	}
	
	@RequestMapping("/admin/account/search")
	public String account_search(HttpServletRequest request, HttpServletResponse response,
			Model model, final RedirectAttributes redirectAttributes){
		
		
		return adminTemplate();
	}
	
	@RequestMapping("/admin/account/edit/{id}")
	public String account_edit(HttpServletRequest request, HttpServletResponse response,
			Model model, final RedirectAttributes redirectAttributes){
		
		
		return adminTemplate();
	}
	
	@RequestMapping("/admin/account/suspend/{id}")
	public String account_delete(HttpServletRequest request, HttpServletResponse response,
			Model model, final RedirectAttributes redirectAttributes){
		
		return adminTemplate();
	}
	
	private String adminTemplate(){
		return "admin_template";
	}
	
	
	*/
	
	
	
	
	
	
	//@RequestMapping("/admin/customer_lisaaat")
	public String admin1(Model model) {
		
		CustomerController customerController = new CustomerController();
		
		/*
		//admin/dashboard
		
		//admin/customer/list
		CustomerController cController = new CustomerController();
		Object cList = cController.getAllCustomer();
		
		//admin/customer/new
		Customer cNew = new Customer();
		
		//admin/customer/edit/123
		Customer cEdit = cController.getCustomerWithAccountByCustomerID(123);
		
		//admin/customer/save/
		//admin/customer/update/123
		ChangesStatus st1 = cController.saveCustomer(new Customer()); //***********
		
		//admin/customer/delete/123
		ChangesStatus st2 = cController.deleteCustomer(123);
		
		//admin/account/list
		AccountController aController = new AccountController();
		aController.getAllAccounts();
		
		//admin/account/new
		Account aNew = new Account();
		
		//admin/account/edit/123
		Account aEdit = aController.getAccountWithOwnerByAccountID(123);
		
		//admin/customer/save/
		//admin/customer/update/123
		ChangesStatus st3 = aController.updateAccount(new Account()); //***********
		
		//admin/customer/delete/123
		ChangesStatus st4 = aController.deleteAccount(123);
		
		//admin/transaction/list
		TransactionController tController = new TransactionController();
		tController.getAllTransaction();
		
		//admin/bank_branch/list
		BankBranchController bController = new BankBranchController();
		
		//admin/bank_branch/edit/123
		
		
		//admin/customer/save/
		//admin/customer/update/123
		
		
		//admin/customer/delete/123
		
		
		///
		///choose_account
		///dashboard
		
		///account/withdraw/123
		cController.getCustomerOnlyByCustomerID(123);
		aController.getAccountOnlyByAccountID(123);
		
		///transaction/withdraw/
		tController.withdraw();
		
		///account/deposit
		aController.getAccountOnlyByAccountID(123);
		
		///transaction/deposit/
		tController.deposti();
		aController.getAccountOnlyByAccountID(123);
		
		///transaction/transfer/
		tController.transfer();
		
		*/
		
		
		///account/transfer
		
		///transaction/list
		
		return "admin_template";
	}

	
	
	//@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String admin_customer_list(Model model,WebRequest w) {
			
		/*HttpServletRequest curRequest = 
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		HttpSession session = request.getSession();*/
		
		PageEnvironment _page = new PageEnvironment();
		//_page.setTitle("Customer List");
		_page.setViewFile("admin/customer_list.jsp");
		
		Message message = new Message("info","adf");
		
		model.addAttribute("_page", _page);
		model.addAttribute("message", message);
		return "admin_template";
	}
	
	
	
	//@RequestMapping(value = "/admin/customer/new", method = RequestMethod.GET)
	/*public String admin_customer_new(Model model) {
		
		PageEnvironment page = new PageEnvironment();
		//page.setTitle("Create New Customer");
		page.setViewFile("admin/customer/edit.jsp");
		
		//Message message = new Message();
		
		model.addAttribute("page", page);
		//model.addAttribute("message", message);
		return "admin_template";
	}
	*/
	//@RequestMapping(value = "/admin/customer/edit/*", method = RequestMethod.GET)
	public String admin_customer_edit(Model model) {
		
		PageEnvironment page = new PageEnvironment();
		//page.setTitle("Create New Customer");
		page.setViewFile("admin/customer/edit.jsp");
		
		//Message message = new Message();
		
		model.addAttribute("page", page);
		//model.addAttribute("message", message);
		return "admin_template";
	}
	
	//@RequestMapping(value = "/admin/customer/save/*", method = RequestMethod.GET)
	public String admin_customer_save(Model model) {
		
		return null;
	}
	
	//@RequestMapping(value = "/admin/customer/delete/*", method = RequestMethod.GET)
	public String admin_customer_delete(Model model) {
		
		return null;
	}
 
}
