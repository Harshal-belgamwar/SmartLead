package project.finanacedashboard.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.finanacedashboard.DTO.Response.Categories.CategorySummaryDTO;
import project.finanacedashboard.DTO.Response.Records.FinancialSummaryDTO;
import project.finanacedashboard.DTO.Response.Records.RecordFilter;
import project.finanacedashboard.DTO.Response.Records.RecordResponse;
import project.finanacedashboard.Entity.Category;
import project.finanacedashboard.Entity.Records;
import project.finanacedashboard.Entity.User;
import project.finanacedashboard.Enum.Type;
import project.finanacedashboard.ExceptionHandler.DataNotFound;
import project.finanacedashboard.Repository.CategoryRepository;
import project.finanacedashboard.Repository.RecordsRepository;
import project.finanacedashboard.Repository.RoleRepository;
import project.finanacedashboard.Repository.UserRepository;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ViewerService {
    CategoryRepository categoryRepository;
    UserRepository userRepository;
    RoleRepository roleRepository;
    RecordsRepository recordsRepository;

    @Autowired
    ViewerService(CategoryRepository categoryRepository, UserRepository userRepository, RoleRepository roleRepository, RecordsRepository recordsRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.recordsRepository = recordsRepository;
    }

//    view all records
    public List<RecordResponse> retrieveAllRecords() {
        Iterable<Records> records = recordsRepository.findAll();
        List<RecordResponse> response = new ArrayList<>();
        for (Records record : records) {
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

        return response;

    }

    public List<RecordResponse> retriveUserRecords(String username){
        User user = userRepository.findByUsername(username).orElseThrow(()-> new DataNotFound("User not found"));
        Iterable<Records> records = recordsRepository.findByUser(user);
        List<RecordResponse> response = new ArrayList<>();
        records.forEach(record -> {
            RecordResponse recordResponse = new RecordResponse();
            if(!record.getIsDeleted()){
                recordResponse.setId(record.getId());
                recordResponse.setCategoryName(record.getCategory().getName());
                recordResponse.setAmount(record.getAmount());
                recordResponse.setType(record.getType());
                recordResponse.setDescription(record.getDescription());
                recordResponse.setUsername(record.getUser().getUsername());
                recordResponse.setDate(record.getCreatedAt());
                response.add(recordResponse);
            }


        });

        return response;
    }
    ////   Total expense , total income , net balance ,
    public FinancialSummaryDTO getSummary(String username) {

        Object[] result = recordsRepository.getFinancialSummary(username);

        BigDecimal totalIncome = (BigDecimal) result[0];
        BigDecimal totalExpense = (BigDecimal) result[1];

        BigDecimal netBalance = totalIncome.subtract(totalExpense);

        return new FinancialSummaryDTO(totalIncome, totalExpense, netBalance);
    }

    public List<CategorySummaryDTO> getCategorySummaryForUser(String username) {

        List<Records> records =
                recordsRepository.findByUserUsernameAndIsDeletedFalse(username);

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



//    ----------------- Filter records --------------------


    public List<Records> filterRecords(RecordFilter filter) {

        List<Records> records;


        if (filter.getUsername() != null) {
            records = recordsRepository
                    .findByUserUsernameAndIsDeletedFalse(filter.getUsername());
        } else {

            records = recordsRepository.findByIsDeletedFalse();
        }

        return records.stream()
                .filter(r -> filter.getStartDate() == null ||
                        !r.getCreatedAt().toLocalDate().isBefore(filter.getStartDate()))
                .filter(r -> filter.getEndDate() == null ||
                        !r.getCreatedAt().toLocalDate().isAfter(filter.getEndDate()))
                .filter(r -> filter.getCategory() == null ||
                        r.getCategory().getName().equalsIgnoreCase(filter.getCategory()))
                .filter(r -> filter.getType() == null ||
                        r.getType() == filter.getType())
                .toList();
    }

}
