package kr.co.sist.upload;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
//@RequestMapping("/day0624")
public class FileController {

	// private String saveDir =
	// "C:/dev/workspace/spring_mvc/src/main/resources/static/upload";
	@Value("${upload.saveDir}")
	private String saveDir;

	@GetMapping("/upload_form")
	public String uploadForm() {
		return "day0624/upload_form";
	} // uploadForm

	@PostMapping("/upload_process")
	public String uploadProcess(@RequestParam("upfile") MultipartFile mf, FileUploadDTO fuDTO, Model model)
			throws Exception {
//		System.out.println("MIME-TYPES : " + mf.getContentType());
//		System.out.println("form control : " + mf.getName());
//		System.out.println("파일명 : " + mf.getOriginalFilename());
//		System.out.println("파일크기 : " + mf.getSize());
//		System.out.println("파일이 비었는지 : " + mf.isEmpty());
//		// 파일명은 DTO에 설정되지 않으므로 setter를 호출하여 설정한다.
//		fuDTO.setFileName(mf.getOriginalFilename());
//		System.out.println("업로더 : " + fuDTO.getUploader());
//		System.out.println("대상나이 : " + Arrays.toString(fuDTO.getTargetAge()));
//		System.out.println("파일명 : " + fuDTO.getFileName());

		fuDTO.setFileName(mf.getOriginalFilename());

		// 이미지만 업로드(설정은 10MByte, 업로드 가능 5MByte)
		if (mf.getContentType().startsWith("image")) {
			int maxSize = 1024 * 1024 * 5;
			if (mf.getSize() > maxSize) {
				throw new Exception("업로드 파일의 크기는 최대 5MByte까지만 가능합니다.");
			} // end if

			// 중복파일명에 처리(동일 파일명이 존재한다면 파일명_숫자.확장자를 붙여 새로 생성)
			// 1. 업로드된 파일명 받기
			String originalFilename = mf.getOriginalFilename();

			// 디렉토리가 존재하지 않으면 디렉토리를 생성
			File dir = new File(saveDir);
			if (!dir.exists()) {
				dir.mkdirs();
			} // end if
				// 2. 파일명으로 파일을 생성
			File uploadFile = new File(saveDir + File.separator + originalFilename);

			// 3. 확장자를 기준으로 파일명을 나눈다.
			String fileName = "";
			String fileExt = "";

			int fileSeperator = originalFilename.lastIndexOf(".");
			if (fileSeperator == -1) { // 확장자가 없다면
				fileSeperator = originalFilename.length();
			} // end if

			fileName = originalFilename.substring(0, fileSeperator);
			if (originalFilename.contains(".")) { // 확장자가 있다면
				fileExt = originalFilename.substring(originalFilename.lastIndexOf("."));
			} // end if

			// 4. 같은 이름의 파일이 존재하면 파일명과 . 사이에 _1(카운트)을 붙여준다.
			int cnt = 1;
			StringBuilder newFileName = new StringBuilder();
			while (uploadFile.exists()) { // 같은 이름의 파일이 존재하면
				newFileName.append(fileName).append("_").append(cnt).append(fileExt);
				uploadFile = new File(saveDir + File.separator + newFileName.toString());
				cnt++;
				newFileName.delete(0, newFileName.length());
			} // end while

			// 5. 업로드 수행
			mf.transferTo(uploadFile); // 중복파일을 처리하지 않는다(덮어쓴다).
			model.addAttribute("uploadFlag", true);
			model.addAttribute("fileSize", mf.getSize());
			model.addAttribute("fileData", fuDTO);
		} // end if

//		return "day0624/upload_result";
		return "upload_result";
	} // uploadProcess

	@GetMapping("/upload_form2")
	public String uploadForm2() {
		return "day0624/upload_form2";
	} // uploadForm

	/*
	 * @PostMapping("/upload_process2") // 여러개의 파일이 업로드 될 때에는 List<MultipartFile>로
	 * 매개변수를 선언 public String uploadProcess2(@RequestParam("upfile")
	 * List<MultipartFile> mfList, FileUploadDTO2 fuDTO, Model model) throws
	 * Exception {
	 * 
	 * int fileCnt = 0; String[] fileNames = new String[mfList.size()]; String[]
	 * viewFileNames = new String[mfList.size()];
	 * 
	 * for (MultipartFile mf : mfList) { // 이미지만 업로드(설정은 10MByte, 업로드 가능 5MByte) if
	 * (mf.getContentType().startsWith("image")) { int maxSize = 1024 * 1024 * 5; if
	 * (mf.getSize() > maxSize) { throw new
	 * Exception("업로드 파일의 크기는 최대 5MByte까지만 가능합니다."); } // end if
	 * 
	 * // 중복파일명에 처리(동일 파일명이 존재한다면 파일명_숫자.확장자를 붙여 새로 생성) // 1. 업로드된 파일명 받기 String
	 * originalFilename = mf.getOriginalFilename(); // 보여줄 파일명을 처리(여러개 선택)
	 * viewFileNames[fileCnt] = originalFilename; // 선택한 파일명(사용자에게 보여줄 이름)
	 * 
	 * // 디렉토리가 존재하지 않으면 디렉토리를 생성 File dir = new File(saveDir); if (!dir.exists()) {
	 * dir.mkdirs(); } // end if // 2. 파일명으로 파일을 생성 File uploadFile = new
	 * File(saveDir + File.separator + originalFilename);
	 * 
	 * // 3. 확장자를 기준으로 파일명을 나눈다. String fileName = ""; String fileExt = "";
	 * 
	 * int fileSeperator = originalFilename.lastIndexOf("."); if(fileSeperator ==
	 * -1) { // 확장자가 없다면 fileSeperator = originalFilename.length(); } // end if
	 * 
	 * fileName = originalFilename.substring(0, fileSeperator); if
	 * (originalFilename.contains(".")) { // 확장자가 있다면 fileExt =
	 * originalFilename.substring(originalFilename.lastIndexOf(".")); } // end if
	 * 
	 * // 4. 같은 이름의 파일이 존재하면 파일명과 . 사이에 _1(카운트)을 붙여준다. int cnt = 1; StringBuilder
	 * newFileName = new StringBuilder(); while (uploadFile.exists()) { // 같은 이름의
	 * 파일이 존재하면 newFileName.append(fileName).append("_").append(cnt)
	 * .append(fileExt); uploadFile = new File(saveDir + File.separator +
	 * newFileName.toString()); cnt++; newFileName.delete(0, newFileName.length());
	 * } // end while
	 * 
	 * // 5. 업로드 수행 mf.transferTo(uploadFile); // 중복파일을 처리하지 않는다(덮어쓴다).
	 * 
	 * fileNames[fileCnt] = uploadFile.getName(); // 업로드된 파일명 설정(중복처리된 이름)
	 * 
	 * 
	 * fileCnt++; // 파일 개수를 증가 } // end if } // end for
	 * 
	 * // 업로드된 파일명을 DTO 설정 fuDTO.setFileName(fileNames); // 중복처리된 파일명을 설정
	 * model.addAttribute("uploadFlag", true); model.addAttribute("viewFileNames",
	 * viewFileNames); // 사용자에게 보여줄 때에는 // 업로드한 파일명을 그대로 제공
	 * 
	 * model.addAttribute("fileData", fuDTO); // 확인차 Model에 입력
	 * 
	 * return "day0624/upload_result2"; } // uploadProcess2
	 * 
	 */

	@ExceptionHandler(Exception.class)
	// spring.servlet.multipart.max-file-size=10MB로 설정된 파일크기보다
	// 업로드된 파일의 크기가 크다면 해당 예외는 처리할 수 없다.
	public ModelAndView uploadErr(Exception e) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/err/upload_err.html");

		e.printStackTrace();
		return mav;
	} // uploadErr
} // class
