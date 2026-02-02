package tech3.binitright.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import tech3.binitright.interfacemethods.AdminInterface;
import tech3.binitright.interfacemethods.CheckInInterface;
import tech3.binitright.model.CheckIn;
import tech3.binitright.request.ReviewRequest;
import tech3.binitright.service.AdminImplementation;
import tech3.binitright.service.CheckInImplementation;
import tech3.binitright.service.ForecastService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
		@Autowired
		private AdminInterface adminService;
		
		public void setAdminService(AdminImplementation adminserviceImp) {
			this.adminService = adminserviceImp;
		}
		
		@Autowired
		private CheckInInterface checkInService;
		
		public void setcheckInService(CheckInImplementation checkInserviceImp) {
			this.checkInService = checkInserviceImp;
		}
        @Autowired
        private  ForecastService forecastService;

        public void setForecastService(ForecastService forecastService) {
            this.forecastService = forecastService;}
		
		/*@GetMapping("/pending")
	    public String getPendingCheckIns(Model model) {
	        List<CheckIn> pendingCheckIns = checkInService.getPendingCheckIns();
	        model.addAttribute("pendingCheckIns", pendingCheckIns);
            Map<String, Object> forecastData = forecastService.getForecastData();

            model.addAttribute("forecastData", forecastData);
	        return "admin-dashboard";
	    }*/
        @GetMapping("/dashboard")
        public String dashboard(Model model) {

            model.addAttribute(
                    "pendingCheckIns",
                    checkInService.getPendingCheckIns()
            );

            model.addAttribute(
                    "forecastData",
                    forecastService.getForecastData()
            );

            return "admin-dashboard";
        }



    @GetMapping("/review/{checkInId}")
	    public String reviewCheckIn(@PathVariable Long checkInId, Model model) {
	        CheckIn checkIn = adminService.reviewCheckIn(checkInId);
	        model.addAttribute("checkIn", checkIn);
	              
	        return "checkin-review";
	    }
		
		@PostMapping("/review/{id}")
		public String reviewDecision(
		        @PathVariable("id") Long checkInId,
		        @RequestParam("status") CheckIn.Status status,
		        @RequestParam(required = false) String remarks,
		        RedirectAttributes redirect) {

		    adminService.updateCheckInStatus(checkInId, status, remarks);

		    redirect.addFlashAttribute(
		        "success",
		        "Check-in " + status.name().toLowerCase() + " successfully"
		    );

		    return "redirect:/admin/pending";
		}

		
}
