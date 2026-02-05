// package tech3.binitright.controller;

// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
// import tech3.binitright.response.RecycleHistoryResponse;
// import tech3.binitright.service.RecycledHistoryService;

// import java.util.List;

// @RestController
// @RequestMapping("/api/recycle-history")
// public class RecycledHistoryController {

//     private RecycledHistoryService service;

//     public RecycledHistoryController(RecycledHistoryService service) {
//         this.service = service;    }

//     @GetMapping("/{userId}")
//     public List<RecycleHistoryResponse> getRecycleHistory(
//             @PathVariable Long userId) {

//         return service.getRecycleHistory(userId);
//     }
// }
