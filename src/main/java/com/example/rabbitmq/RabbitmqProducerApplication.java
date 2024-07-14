package com.example.rabbitmq;

import com.example.rabbitmq.encryption.DecryptRsa;
import com.example.rabbitmq.persistence.CacheService;
import com.example.rabbitmq.persistence.StorageService;
import com.example.rabbitmq.student.Student;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitmqProducerApplication {

	private static StorageService storageService;
	private static CacheService cacheService;
	private static DecryptRsa rsa;

	public RabbitmqProducerApplication(StorageService storageService, CacheService redisService, DecryptRsa rsa) {
		RabbitmqProducerApplication.storageService = storageService;
		RabbitmqProducerApplication.cacheService = redisService;
		RabbitmqProducerApplication.rsa = rsa;
	}

	public static void main(String[] args) {
		SpringApplication.run(RabbitmqProducerApplication.class, args);

		Student encryptedStudent = new Student();
		encryptedStudent.setId(99);
		encryptedStudent.setFirstName("fN2T6mrPzLlxPUPyvZcdyPI1bvG1pS7ErvJfo274gtyZYGGuyQdlwizmA7PtC3iHwlQfS1d48vP/Xr/YRbn9eMocVxkm0iyF/Erb50oI+4JU5oKj/FVhgUIH4omTT0wthGX6jiRZ+4VoSl5wdS4TJIdzxm0Ch+q0Z1lxrbNqZFy/GYwI6FcqdxFZk30PBiU82RvTmofLekpy4qYwYtliXWDayC7ICfftR4sdJEfPRR68rvyRrqsuQsLp5M2CmCADvVc45/4h0RpDgqEWJh5h4P9iEitCxK4XV480/SP4t7XLoUTZdgdhBzHmHTtU57BGVD2LyVFJt78/9FkM1BiBYA==");
		encryptedStudent.setLastName("Doe");
		encryptedStudent.setAge(19);
		encryptedStudent.setEmail("ggQ096KjwKz2ULuHHchwgW+knNVNdvmpq9cYZDydnCztZwD24nX1A2zQ8HCOX/niaYJpqcmpWeXxU7uyELiftRfyecbH1y4aZrAND/Flb72kO/TZCBZpwo9V/s6PMj6MXkSFUW9vYw8Io6EHHfmTTIbPYLrZHBm1gHN2iKutxdYZcOtIHAwEXTIjXPrQ45VgGwY9z6DiZHrp3CdY05GMdhRypOU424mJvjRT71DIuRVJfy2GGk/EK5mJlgWQgZbcDemf15RMHGpUCpp3I7VU7c2BgBnl2b+H8mcFd4Nj8N9OP1H9qslEE9lrXPIEwQYYW0L4LPSFlIL85cELfgRqzw==");

		try {
			String decryptedFirstname = rsa.decrypt(encryptedStudent.getFirstName());
			String decryptedEmail = rsa.decrypt(encryptedStudent.getEmail());
			System.out.println("Decrypted student firstname: " + decryptedFirstname);
			System.out.println("Decrypted student email: " + decryptedEmail);

			Student decryptedStudent = new Student(decryptedFirstname, encryptedStudent.getLastName(), encryptedStudent.getAge(), decryptedEmail);
			// cacheService.set("1", decryptedStudent, 10L);
			// StorageService storageService = new StorageService();
			// storageService.saveStudent(decryptedStudent);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
