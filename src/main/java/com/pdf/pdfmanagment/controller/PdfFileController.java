package com.pdf.pdfmanagment.controller;


import com.pdf.pdfmanagment.entity.Comment;
import com.pdf.pdfmanagment.entity.PdfFile;
import com.pdf.pdfmanagment.entity.User;
import com.pdf.pdfmanagment.repository.CommentRepository;
import com.pdf.pdfmanagment.repository.PdfFileRepository;
import com.pdf.pdfmanagment.repository.UserRepository;
import com.pdf.pdfmanagment.service.CommentService;
import com.pdf.pdfmanagment.service.PdfFileService;
import com.pdf.pdfmanagment.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;


import javax.swing.text.html.Option;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class PdfFileController {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PdfFileRepository pdfFileRepository;
    @Autowired
    private PdfFileService pdfFileService;
    @Autowired
    private final CommentService commentService;
    @Autowired
    private final CommentRepository commentRepository;
    @Autowired
    private final HttpServletRequest request;

    public PdfFileController(JavaMailSender mailSender, UserService userService, UserRepository userRepository, PdfFileRepository pdfFileRepository, PdfFileService pdfFileService, CommentService commentService, CommentRepository commentRepository, HttpServletRequest request) {
        this.mailSender = mailSender;
        this.userService = userService;
        this.userRepository = userRepository;
        this.pdfFileRepository = pdfFileRepository;
        this.pdfFileService = pdfFileService;
        this.commentService = commentService;
        this.commentRepository = commentRepository;
        this.request = request;
    }



    // handler method to handle list of users
//    @GetMapping("/dashboard")
//    public String users(Model model){
////        List<UserDto> users = userService.findAllUsers();
////        model.addAttribute("users", users);
//        return "users";
//    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal, @RequestParam(value = "searchKeyword", required = false) String searchKeyword) {
        // Get the authenticated user
        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        // Get the list of PDF files uploaded by the user
        List<PdfFile> userPdfFiles = pdfFileService.getPdfFilesByUser(user);

        // Add the list of PDF files to the model
        model.addAttribute("userPdfFiles", userPdfFiles);

        // Perform search if searchKeyword is provided
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            // Perform search in all PDF files
            List<PdfFile> searchResults = pdfFileService.searchPdfFilesByUserAndFileName(user,searchKeyword);
//            List<PdfFile> searchResults = pdfFileService.searchPdfFilesByUserIdAndFileName(user, searchKeyword);
//            System.out.println("Search result size: " + searchResults.size());

            // Add the search results to the model
            model.addAttribute("searchResults", searchResults);
        }

        // Return the dashboard view
        return "dashboard";
    }




    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Principal principal, RedirectAttributes redirectAttributes) {
        // Validate that the uploaded file is a PDF
        if (!file.getContentType().equalsIgnoreCase("application/pdf")) {
            // Handle the error, return an error message, or redirect the user to an error page
            redirectAttributes.addFlashAttribute("error", "Please upload a PDF file.");

            return "redirect:/dashboard";
        }

        // Get the authenticated user
        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        try {
            // Create a new PdfFile entity and set its properties
            PdfFile uploadedFile = new PdfFile();
            uploadedFile.setFileName(file.getOriginalFilename());
            uploadedFile.setFileType(file.getContentType());
            uploadedFile.setData(file.getBytes());

            // Generate a unique link
            String uniqueLink = generateUniqueLink();
            uploadedFile.setUniqueLink(uniqueLink);
            uploadedFile.setUser(user);

            // Save the PdfFile entity to the database
            pdfFileRepository.save(uploadedFile);

            // Handle success, return a success message, or redirect the user to a success page
        } catch (IOException e) {
            // Handle the error, return an error message, or redirect the user to an error page

            redirectAttributes.addFlashAttribute("errorMessage", "Error uploading the file.");

            // Return the dashboard view with the error message
            return "dashboard";
        }

        return "redirect:/dashboard";
    }

    public String generateUniqueLink() {
        // Generate a unique random UUID
        UUID uuid = UUID.randomUUID();

        // Convert the UUID to a string
        String uniqueLink = uuid.toString();

        return uniqueLink;
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long fileId, Principal principal) {
        // Get the authenticated user
        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        // Find the file by its ID and check if the user is authorized to download it
        Optional<PdfFile> optionalFile = pdfFileRepository.findById(fileId);
//        if (optionalFile.isEmpty() || !optionalFile.get().getUser().equals(user)) {
//            // Handle the error, return an error message, or redirect the user to an error page
//        }

        // Create a ByteArrayResource with the file data
        PdfFile file = optionalFile.get();
        ByteArrayResource resource = new ByteArrayResource(file.getData());

        // Set the Content-Disposition header to force the browser to download the file
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getFileName());

        // Return the file as a ResponseEntity
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.getData().length)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @GetMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable Long fileId, Principal principal)    {

        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        Optional<PdfFile> optionalFile = pdfFileRepository.findById(fileId);
        PdfFile file = optionalFile.get();
        pdfFileRepository.delete(file);
        return "redirect:/dashboard";

    }



//    @GetMapping("/shared/{uniqueLink}")
//    public String viewSharedPdfFile(@PathVariable("uniqueLink") String uniqueLink, Model model) {
//        // Find the PDF file based on the unique link
//        PdfFile pdfFile = pdfFileRepository.findByUniqueLink(uniqueLink);
//
//        // Check if the PDF file exists and is accessible
//        if (pdfFile != null) {
//            // Add the PDF file to the model
//            model.addAttribute("pdfFile", pdfFile);
//
//            // Return the view for viewing the shared file
//            return "viewSharedFile";
//        } else {
//            // Return an error view or redirect to an error page
//            return "error";
//        }
//    }

    @GetMapping("/shared/{uniqueCode}")
    public String viewSharedPdfFile(@PathVariable String uniqueCode, HttpServletRequest request,Model model) {
        HttpSession session = request.getSession();
        String targetUrl = "/shared/" + uniqueCode; // Set the target URL
        session.setAttribute("targetUrl", targetUrl);
        // Check if the user is authenticated
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication instanceof AnonymousAuthenticationToken || !authentication.isAuthenticated()) {
//            // Redirect the user to the login or registration page
////            return "redirect:/login"; // Replace with the appropriate URL
//            String loginUrl = "/login?targetUrl=" + UriUtils.encodePath(targetUrl, StandardCharsets.UTF_8);
//            return "redirect:" + loginUrl;
//        }

        // Get the authenticated user's information
//        String userEmail = authentication.getName(); // Assuming the email is used as the username

        // Retrieve the PDF file by unique code
        PdfFile pdfFile = pdfFileService.findByUniqueLink(uniqueCode);
        if (pdfFile == null) {
            // Handle invalid unique code or PDF file not found

            model.addAttribute("error", "The requested PDF file is not present");
            model.addAttribute("message", "please select valid PDF file from list");
            return "error"; // Replace with the appropriate error page or redirect
        }

        String base64Data = Base64.getEncoder().encodeToString(pdfFile.getData());

        // Pass the base64Data to the viewpdffile.html template
        model.addAttribute("base64Data", base64Data);
        // Add the PDF file to the model
        model.addAttribute("pdfFile", pdfFile);
        System.out.println("shared file ");
        // Return the view PDF file template
        return "viewpdffile"; // Replace with the appropriate template name
    }




    @GetMapping("/view/{uniqueCode}")
    public String viewPDF(@PathVariable("uniqueCode") String uniqueCode, Model model) {
        // Retrieve the PdfFile object
//        Optional<PdfFile> pdfFileOptional = pdfFileService.findById(fileId);

        PdfFile pdfFile = pdfFileService.findByUniqueLink(uniqueCode);

        // Check if the PdfFile exists
        if (pdfFile != null) {

            // Convert the byte array to Base64 string
            String base64Data = Base64.getEncoder().encodeToString(pdfFile.getData());

            // Pass the base64Data to the viewpdffile.html template
            model.addAttribute("base64Data", base64Data);
            model.addAttribute("pdfFile", pdfFile);
            return "viewpdffile";
        } else {
            // Handle the case when the PdfFile does not exist


            model.addAttribute("error", "The requested PDF file is not present");
            model.addAttribute("message", "please select valid PDF file from list");
            return "error"; // You can create an error.html template to display an error message
        }
    }

//    @PostMapping("/comments/add")
//    public String addComment(@RequestParam("pdfFileId") Long pdfFileId, @RequestParam("commentText") String commentText, Principal principal) {
//
//        String email = principal.getName();
//        User user = userRepository.findByEmail(email);
//
//        commentRepository.addComment(pdfFileId, commentText, user);
//
//        return "redirect:/view/" + pdfFileId;
//    }

    @PostMapping("/comments/add")
    public String addCommentToPDF(@RequestParam("pdfFileId") Long pdfFileId, @RequestParam("commentText") String commentText, Principal principal) {
        try {
//             Check if the user is authenticated
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken || !authentication.isAuthenticated()) {
            // Redirect the user to the login or registration page
//            return "redirect:/login"; // Replace with the appropriate URL
            return "redirect:/login/";
        }
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            PdfFile pdfFile= pdfFileService.getPdfFileById(pdfFileId);
            String uniqueLink=pdfFile.getUniqueLink();
            Comment comment = new Comment(pdfFile,user,commentText); // Create a new comment

            pdfFileService.addCommentToPDF(comment, pdfFileId);
            return "redirect:/view/" + uniqueLink;
        } catch (Exception e) {
            return "error";
        }
    }

//    @PostMapping("/comments/add")
//    public String addCommentToPDF(@RequestParam("pdfFileId") Long pdfFileId,
//                                  @RequestParam("commentText") String commentText,
//                                  @RequestParam(value = "parentId", required = false) Long parentId,
//                                  Principal principal) {
//        try {
//            String email = principal.getName();
//            User user = userRepository.findByEmail(email);
//            PdfFile pdfFile = pdfFileService.getPdfFileById(pdfFileId);
//
//            if (parentId != null) {
//                // If parentId is provided, it means it's a reply
//                Comment parentComment = commentService.getCommentById(parentId);
//                if (parentComment == null) {
//                    // Handle case when parent comment is not found
//                    return "error";
//                }
//
//                Comment reply = new Comment(pdfFile, user, commentText); // Create a new reply
//                parentComment.addReply(reply); // Add the reply to the parent comment
//
//                pdfFileService.addCommentToPDF(reply, pdfFileId);
//            } else {
//                // It's a new comment
//                Comment comment = new Comment(pdfFile, user, commentText); // Create a new comment
//                pdfFileService.addCommentToPDF(comment, pdfFileId);
//            }
//
//            return "redirect:/view/" + pdfFileId;
//        } catch (Exception e) {
//            return "error";
//        }
//    }





}
