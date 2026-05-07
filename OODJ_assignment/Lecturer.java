public class Lecturer {
    private String id;
    private String name;
    private String email;
    private String password;

    public Lecturer(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // CSV line -> Lecturer object
    public static Lecturer fromCsvLine(String line) {
        String[] p = line.split(",");
        if (p.length < 10) return null;

        String role = p[8].trim();
        if (!role.equalsIgnoreCase("lecturer")) return null;

        String id = p[0].trim();
        String password = p[2].trim();
        String name = p[3].trim();
        String email = p[5].trim();

        return new Lecturer(id, name, email, password);
    }
}
