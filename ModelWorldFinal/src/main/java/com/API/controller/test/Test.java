//import org.bytedeco.opencv.opencv_core.*;
//import org.bytedeco.opencv.global.opencv_core.*;
//import org.bytedeco.opencv.global.opencv_imgcodecs.*;
//import org.bytedeco.opencv.global.opencv_imgproc.*;
//import org.bytedeco.opencv.global.opencv_objdetect.*;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//
//@RestController
//@RequestMapping("/api/face-compare")
//public class Test {
//
//    private final String haarCascadePath = "src/main/resources/haarcascade_frontalface_default.xml";
//
//    @PostMapping("/compare")
//    public ResponseEntity<String> compareFaces(
//            @RequestParam("image1") MultipartFile image1,
//            @RequestParam("image2") MultipartFile image2) {
//
//        try {
//            // Lưu file ảnh từ MultipartFile
//            File file1 = saveFile(image1);
//            File file2 = saveFile(image2);
//
//            // Phát hiện và cắt khuôn mặt
//            Mat face1 = detectAndExtractFace(file1.getAbsolutePath());
//            Mat face2 = detectAndExtractFace(file2.getAbsolutePath());
//
//            // So sánh khuôn mặt
//            boolean isMatch = compareFaces(face1, face2);
//
//            // Xóa file tạm
//            file1.delete();
//            file2.delete();
//
//            // Trả về kết quả
//            return ResponseEntity.ok(isMatch ? "Match" : "No Match");
//
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Error: " + e.getMessage());
//        }
//    }
//
//    private File saveFile(MultipartFile file) throws IOException {
//        File tempFile = File.createTempFile("upload-", ".jpg");
//        file.transferTo(tempFile);
//        return tempFile;
//    }
//
//    private Mat detectAndExtractFace(String imagePath) {
//        // Tải ảnh
//        Mat image = imread(imagePath);
//
//        // Load Haar Cascade
//        CascadeClassifier faceDetector = new CascadeClassifier(haarCascadePath);
//        RectVector faceDetections = new RectVector();
//
//        // Phát hiện khuôn mặt
//        faceDetector.detectMultiScale(image, faceDetections);
//
//        // Nếu tìm thấy khuôn mặt, cắt khuôn mặt
//        if (faceDetections.size() > 0) {
//            Rect rect = faceDetections.get(0); // Chọn khuôn mặt đầu tiên
//            return new Mat(image, rect);
//        }
//
//        throw new RuntimeException("No face detected in the image!");
//    }
//
//    private boolean compareFaces(Mat face1, Mat face2) {
//        // Resize để đảm bảo kích thước giống nhau
//        Mat resizedFace1 = new Mat();
//        Mat resizedFace2 = new Mat();
//
//        resize(face1, resizedFace1, new Size(100, 100));
//        resize(face2, resizedFace2, new Size(100, 100));
//
//        // Tính toán sự khác biệt giữa hai khuôn mặt
//        Mat diff = new Mat();
//        absdiff(resizedFace1, resizedFace2, diff);
//
//        Scalar sumDiff = sumElems(diff);
//        double difference = sumDiff.get(0) + sumDiff.get(1) + sumDiff.get(2);
//
//        // Ngưỡng để xác định có khớp hay không
//        return difference < 5000; // Tùy chỉnh ngưỡng phù hợp
//    }
//}
