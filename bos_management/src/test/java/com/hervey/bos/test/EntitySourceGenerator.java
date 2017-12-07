package com.hervey.bos.test;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;

import org.junit.Test;

public class EntitySourceGenerator {
    private String entity;
    private String entityID;
    private String home;
    private String basePackageName;
    private String entityFirstLower;
    private String entityAction;
    private String entityService;
    private String entityServiceImpl;
    private String entityDao;

    private static final String DAO = "Repository";
    private static final String SERVICE = "Service";
    private static final String ACTION = "Action";
    private static final String SERVICEIMPL = "ServiceImpl";

    // 构造方法，加载属性文件，查找路径
    public EntitySourceGenerator() {
        Properties prop = new Properties();
        try {
            InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("sourceCodeGeneratorConfig.properties");
            prop.load(inStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please input your entityName and entityName ID Type.\r\n" +
                "Example Area,String\r\n" +
                "-->");
        String inputValue = scanner.next().trim();
        String[] arrays = inputValue.split(",");
        entity = arrays[0];
        entityID = arrays[1];
        home = prop.getProperty("home");
        basePackageName = prop.getProperty("basePackageName");
        entityFirstLower = this.getFirstLower(entity);
        entityAction = entity + "Action";
        entityService = entity + "Service";
        entityServiceImpl = entity + "ServiceImpl";
        entityDao = entity + "Repository";
    }

    /**
     * 生成所有的java文件（action类、service接口、service的实现类、dao接口）
     *
     * @throws Exception
     */
    @Test
    public void generateAll() throws Exception {
        this.generateAction();
        this.generateDao();
        this.generateService();
        this.generateServiceImpl();
    }

    /**
     * 生成dao和dao impl文件
     *
     * @throws Exception
     */
    @Test
    public void generateDaoAndDaoImpl() throws Exception {
        this.generateDao();
    }

    /**
     * 生成service和service impl文件
     *
     * @throws Exception
     */
    @Test
    public void generateServiceAndServiceImpl() throws Exception {
        this.generateService();
        this.generateServiceImpl();
    }

    /**
     * 生成action文件
     *
     * @throws Exception
     */
    @Test
    public void generateAction() throws Exception {
        try {
            PrintWriter writer = this.getWriter(ACTION);
            writer.println("@ParentPackage(value=\"json-default\")");
            writer.println("@Namespace(\"/\")");
            writer.println("@Controller");
            writer.println("@Scope(\"prototype\")");
            writer.println("public class " + entityAction
                    + " extends BaseAction<" + entity + "> {");
            writer.println("");
            writer.println("\t@Autowired");
            writer.println("\tprivate " + entityService + " "
                    + entityFirstLower + SERVICE + ";");
            writer.println("}");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成service文件
     *
     * @throws Exception
     */
    @Test
    public void generateService() throws Exception {
        try {
            PrintWriter writer = this.getWriter(SERVICE);
            writer.println("public interface " + entityService + " {");
            writer.println("");
            writer.println("");
            writer.println("}");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成service impl文件
     *
     * @throws Exception
     */
    @Test
    public void generateServiceImpl() throws Exception {
        try {
            PrintWriter writer = this.getWriter(SERVICEIMPL);
            writer.println("@Service");
            writer.println("@Transactional");
            writer.println("public class " + entityServiceImpl + " implements "
                    + entityService + " {");
            writer.println("");
            writer.println("\t@Autowired");
            writer.println("\tprivate " + entityDao + " "
                    + entityFirstLower + DAO + ";");
            writer.println("");
            writer.println("}");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成dao文件
     *
     * @throws Exception
     */
    @Test
    public void generateDao() throws Exception {
        try {
            PrintWriter writer = this.getWriter(DAO);
            writer.println("public interface " + entityDao
                    + " extends JpaRepository<" + entity + "," + entityID + ">,JpaSpecificationExecutor<" + entity + "> {");
            writer.println("");
            writer.println("");
            writer.println("}");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFirstLower(String entity) {
        char[] chars = entity.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return String.valueOf(chars);
    }

    private PrintWriter getWriter(String generatorObejctName) throws IOException {
        String packagePath = null;
        String packageName = null;
        String filename = null;
        if (generatorObejctName.equals(ACTION)) {
            packageName = basePackageName + ".web.action";
            filename = entityAction + ".java";
        } else if (generatorObejctName.equals(SERVICE)) {
            packageName = basePackageName + ".service";
            filename = entityService + ".java";
        } else if (generatorObejctName.equals(SERVICEIMPL)) {
            packageName = basePackageName + ".service.impl";
            filename = entityServiceImpl + ".java";
        } else if (generatorObejctName.equals(DAO)) {
            packageName = basePackageName + ".dao";
            filename = entityDao + ".java";
        } else {
            throw new RuntimeException("Invalid arguments " + generatorObejctName);
        }
        packagePath = packageName.replaceAll("\\.", "/");
        String path = home + "/" + packagePath + "/" + filename;
        File file = new File(path);
        if (!file.exists()) {
            System.out.println(file.getPath());
            file.createNewFile();
        } else {
            throw new IOException("File already exists :" + path);
        }
        PrintWriter writer = new PrintWriter(file);
        writer.println("package " + packageName + ";");
        writer.println("");
        return writer;
    }


    public static void main(String[] args) {
        EntitySourceGenerator generator = new EntitySourceGenerator();
        try {
            generator.generateAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
