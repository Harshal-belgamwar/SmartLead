package project.finanacedashboard.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.finanacedashboard.DTO.Response.Categories.CategorySummaryDTO;
import project.finanacedashboard.DTO.Response.Records.FinancialSummaryDTO;
import project.finanacedashboard.DTO.Response.Records.RecordFilter;
import project.finanacedashboard.DTO.Response.Records.RecordResponse;
import project.finanacedashboard.Entity.Records;
import project.finanacedashboard.Services.AdminService;
import project.finanacedashboard.Services.ViewerService;

import java.util.List;

@RestController
@RequestMapping("/analyst")
public class AnalystController {

    private final AdminService adminService;
    private final ViewerService viewerService;

    @Autowired
    public AnalystController(AdminService adminService, ViewerService viewerService) {
        this.adminService = adminService;
        this.viewerService = viewerService;
    }

    // Analyst can access methods of admin like fetch all records for expense
    @GetMapping("/records/expenses")
    public List<RecordResponse> getAllExpenseRecords() {
        return adminService.getAllExpenseRecords();
    }

    // Analyst can access all methods of viewer
    @GetMapping("/records")
    public List<RecordResponse> getAllRecords() {
        return viewerService.retrieveAllRecords();
    }

    @GetMapping("/records/{username}")
    public List<RecordResponse> getUserRecords(@PathVariable String username) {
        return viewerService.retriveUserRecords(username);
    }

    @GetMapping("/summary/{username}")
    public FinancialSummaryDTO getUserSummary(@PathVariable String username) {
        return viewerService.getSummary(username);
    }

    @GetMapping("/category-summary/{username}")
    public List<CategorySummaryDTO> getCategorySummaryForUser(@PathVariable String username) {
        return viewerService.getCategorySummaryForUser(username);
    }

    @PostMapping("/filter")
    public List<Records> filterRecords(@RequestBody RecordFilter filter) {
        return viewerService.filterRecords(filter);
    }
}
