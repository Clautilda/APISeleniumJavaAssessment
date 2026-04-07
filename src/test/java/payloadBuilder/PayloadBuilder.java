package payloadBuilder;

public class PayloadBuilder {
    public static String loginUserPayload(String email, String password) {
        return "{\n" +
                "  \"email\": \"" + email + "\",\n" +
                "  \"password\": \"" + password + "\"\n" +
                "}";
    }
    public static String registerUserPayload(String firstName, String lastName, String email, String password, String confirmPassword, String groupId) {
        return "{\n" +
                "  \"firstName\": \"" + firstName + "\",\n" +
                "  \"lastName\": \"" + lastName + "\",\n" +
                "  \"email\": \"" + email + "\",\n" +
                "  \"password\": \"" + password + "\",\n" +
                "  \"confirmPassword\": \"" + confirmPassword + "\",\n" +
                "  \"groupId\": \"" + groupId + "\"\n" +
                "}";
    }

    public static String updateUserRolePayload(String role) {
        return "{\n" +
                "  \"role\": \"" + role + "\"\n" +
                "}";
    }


}
