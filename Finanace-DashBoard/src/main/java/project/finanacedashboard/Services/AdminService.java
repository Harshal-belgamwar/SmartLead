package project.finanacedashboard.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import project.finanacedashboard.DTO.Request.Category.CategoryRequest;
import project.finanacedashboard.DTO.Request.RecordRequest;
import project.finanacedashboard.DTO.Request.RoleRequest;
import project.finanacedashboard.DTO.Request.UpdateRole;
import project.finanacedashboard.DTO.Response.Categories.CategoryResponse;
import project.finanacedashboard.DTO.Response.Categories.CategorySummaryDTO;
import project.finanacedashboard.DTO.Response.DataResponse;
import project.finanacedashboard.DTO.Response.Records.FinancialSummaryDTO;
import project.finanacedashboard.DTO.Response.Records.RecordResponse;
import project.finanacedashboard.Entity.Category;
import project.finanacedashboard.Entity.Records;
import project.finanacedashboard.Entity.Role;
import project.finanacedashboard.Entity.User;
import project.finanacedashboard.Enum.Type;
import project.finanacedashboard.ExceptionHandler.DataAlreadyExist;
import project.finanacedashboard.ExceptionHandler.DataNotFound;
import project.finanacedashboard.Repository.CategoryRepository;
import project.finanacedashboard.Repository.RecordsRepository;
import project.finanacedashboard.Repository.RoleRepository;
import project.finanacedashboard.Repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    CategoryRepository categoryRepository;
    UserRepository userRepository;
    RoleRepository roleRepository;
    RecordsRepository recordsRepository;

    @Autowired
    AdminService(CategoryRepository categoryRepository, UserRepository userRepository, RoleRepository roleRepository, RecordsRepository recordsRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.recordsRepository = recordsRepository;
    }

    // create Role
    public DataResponse AddRole(@RequestBody RoleRequest roleRequest) {
        String role1 = roleRequest.getRole().toUpperCase();

        if(roleRepository.existsByRole(role1)){
            throw new DataAlreadyExist("Role already exist");
        }

        Role role = new Role();
        role.setRole(role1);
        roleRepository.save(role);

        return new DataResponse("Role added successfully");
    }

    //update Role
    public DataResponse updateRole(Long id, UpdateRole updateRole) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new DataNotFound("Role not found"));

        String updatedRole = updateRole.getNewRole().toUpperCase();

        // check duplicate (optional but good)
        if (roleRepository.existsByRole(updatedRole)) {
            throw new DataAlreadyExist("New Role already exists");
        }

        role.setRole(updatedRole);
        roleRepository.save(role);

        return new DataResponse("Role updated successfully");
    }

    // delete role
    public DataResponse deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new DataNotFound("Role not found"));

        //clear all references
        userRepository.UpdateRole(id);

        roleRepository.delete(role);

        return new DataResponse("Role deleted successfully");

    }

    // view all roles
    public List<Role> viewAllRoles() {
        List<Role> arr = new ArrayList<>();
        Iterable<Role> roles = roleRepository.findAll();
        roles.forEach(arr::add);
        return arr;

    }

//    ---------  categories --------------
//    create category
    public CategoryResponse create(CategoryRequest req) {

        if (categoryRepository.existsByName(req.getName())) {
            throw new DataAlreadyExist("Category already exists");
        }

        Category category = new Category();
        category.setName(req.getName());
        category.setType(Type.valueOf(req.getType().toUpperCase()));
        category.setCreatedAt(LocalDateTime.now());

        categoryRepository.save(category);
        CategoryResponse categoryResponse = new  CategoryResponse();
        categoryResponse.setName(req.getName());
        categoryResponse.setType(req.getType());
        return categoryResponse;
    }

//    Update category
    public CategoryResponse update(Long id,CategoryRequest req) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFound("Category not found"));

        category.setName(req.getName());
        category.setType(Type.valueOf(req.getType().toUpperCase()));

        categoryRepository.save(category);
        CategoryResponse categoryResponse = new  CategoryResponse();
        categoryResponse.setName(req.getName());
        categoryResponse.setType(req.getType());

        return categoryResponse;
    }

//    delete category
    public ResponseEntity<?>  delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFound("Category not found"));
        recordsRepository.updateRecord(id);

        categoryRepository.delete(category);
        return ResponseEntity.ok("successfully deleted!");

    }

//    -------------- Records ----------------------

    public ResponseEntity<?>  createRecord(RecordRequest recordRequest) {
        Category cat = categoryRepository.findByName(recordRequest.getCategoryName()).orElseThrow(()-> new DataNotFound("Category not found"));

        User user = userRepository.findByUsername(recordRequest.getUsername()).orElseThrow(()-> new DataNotFound("User not found"));

        Records record = new Records();
        record.setCategory(cat);
        record.setUser(user);
        record.setCreatedAt(LocalDateTime.now());
        record.setAmount(recordRequest.getAmount());
        record.setDescription(recordRequest.getDescription());
        record.setType(recordRequest.getType());

        recordsRepository.save(record);

        return ResponseEntity.ok("successfully Record added!");
    }

    // delete record
    public ResponseEntity<?>  DeleteRecord(Long id) {
        if(!recordsRepository.existsById(id)){ throw new DataNotFound("Record not found");}
        recordsRepository.deleteById(id);
        return ResponseEntity.ok("successfully deleted!");
    }

    // update record
    public ResponseEntity<?>  updateRecord(RecordRequest recordRequest) {
        if(!recordsRepository.existsById(recordRequest.getId())){ throw new DataNotFound("Record not found");}

        Records record = recordsRepository.findById(recordRequest.getId()).get();
        if(record.getIsDeleted()){
            throw new DataNotFound("Record not found");
        }
        Category cat = categoryRepository.findByName(recordRequest.getCategoryName()).orElseThrow(()-> new DataNotFound("Category not found"));
        record.setCategory(cat);
        record.setCreatedAt(LocalDateTime.now());
        record.setAmount(recordRequest.getAmount());
        record.setDescription(recordRequest.getDescription());
        record.setType(recordRequest.getType());

        recordsRepository.save(record);
        return ResponseEntity.ok("successfully updated!");

    }

//    Total expense , total income , net balance ,
    public FinancialSummaryDTO getSummary() {

        Object[] result = recordsRepository.getGlobalFinancialSummary();

        BigDecimal totalIncome = (BigDecimal) result[0];
        BigDecimal totalExpense = (BigDecimal) result[1];

        BigDecimal netBalance = totalIncome.subtract(totalExpense);

        return new FinancialSummaryDTO(totalIncome, totalExpense, netBalance);
    }

//    category wise amount
    public List<CategorySummaryDTO> getCategorySummary() {

        List<Records> records = recordsRepository.findByIsDeletedFalse();

        Map<String, BigDecimal> map = new HashMap<>();

        for (Records r : records) {
            String key = r.getCategory().getName();

            map.put(key,
                    map.getOrDefault(key, BigDecimal.ZERO).add(r.getAmount()));
        }

        List<CategorySummaryDTO> result = new ArrayList<>();

        for (Map.Entry<String, BigDecimal> entry : map.entrySet()) {
            result.add(new CategorySummaryDTO(
                    entry.getKey(),
                    entry.getValue()
            ));
        }

        return result;
    }

    // fetch all records for expense
    public List<RecordResponse> getAllExpenseRecords() {
        List<Records> records = recordsRepository.findByIsDeletedFalse();
        List<RecordResponse> response = new ArrayList<>();

        for (Records record : records) {
            if (record.getType() == Type.EXPENSE) {
                RecordResponse recordResponse = new RecordResponse();
                recordResponse.setId(record.getId());
                recordResponse.setCategoryName(record.getCategory().getName());
                recordResponse.setAmount(record.getAmount());
                recordResponse.setType(record.getType());
                recordResponse.setDescription(record.getDescription());
                recordResponse.setUsername(record.getUser().getUsername());
                recordResponse.setDate(record.getCreatedAt());
                response.add(recordResponse);
            }
        }
        return response;
    }

}
