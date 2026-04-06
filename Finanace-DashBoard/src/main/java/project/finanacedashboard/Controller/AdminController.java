package project.finanacedashboard.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.finanacedashboard.DTO.Request.Category.CategoryRequest;
import project.finanacedashboard.DTO.Request.RecordRequest;
import project.finanacedashboard.DTO.Request.RoleRequest;
import project.finanacedashboard.DTO.Request.UpdateRole;
import project.finanacedashboard.DTO.Response.Categories.CategoryResponse;
import project.finanacedashboard.DTO.Response.Categories.CategorySummaryDTO;
import project.finanacedashboard.DTO.Response.DataResponse;
import project.finanacedashboard.DTO.Response.Records.FinancialSummaryDTO;
import project.finanacedashboard.DTO.Response.Records.RecordResponse;
import project.finanacedashboard.Entity.Role;
import project.finanacedashboard.Services.AdminService;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // Role Management
    @PostMapping("/roles")
    public DataResponse addRole(@RequestBody RoleRequest roleRequest) {
        return adminService.AddRole(roleRequest);
    }

    @PutMapping("/roles/{id}")
    public DataResponse updateRole(@PathVariable Long id, @RequestBody UpdateRole updateRole) {
        return adminService.updateRole(id, updateRole);
    }

    @DeleteMapping("/roles/{id}")
    public DataResponse deleteRole(@PathVariable Long id) {
        return adminService.deleteRole(id);
    }

    @GetMapping("/roles")
    public List<Role> viewAllRoles() {
        return adminService.viewAllRoles();
    }

    // Category Management
    @PostMapping("/categories")
    public CategoryResponse createCategory(@RequestBody CategoryRequest req) {
        return adminService.create(req);
    }

    @PutMapping("/categories/{id}")
    public CategoryResponse updateCategory(@PathVariable Long id, @RequestBody CategoryRequest req) {
        return adminService.update(id, req);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        return adminService.delete(id);
    }

    // Records Management
    @PostMapping("/records")
    public ResponseEntity<?> createRecord(@RequestBody RecordRequest recordRequest) {
        return adminService.createRecord(recordRequest);
    }

    @PutMapping("/records")
    public ResponseEntity<?> updateRecord(@RequestBody RecordRequest recordRequest) {
        return adminService.updateRecord(recordRequest);
    }

    @DeleteMapping("/records/{id}")
    public ResponseEntity<?> deleteRecord(@PathVariable Long id) {
        return adminService.DeleteRecord(id);
    }

    // Summaries
    @GetMapping("/summary")
    public FinancialSummaryDTO getGlobalSummary() {
        return adminService.getSummary();
    }

    @GetMapping("/category-summary")
    public List<CategorySummaryDTO> getCategorySummary() {
        return adminService.getCategorySummary();
    }

    // Fetch all records for expense
    @GetMapping("/records/expenses")
    public List<RecordResponse> getAllExpenseRecords() {
        return adminService.getAllExpenseRecords();
    }
}
