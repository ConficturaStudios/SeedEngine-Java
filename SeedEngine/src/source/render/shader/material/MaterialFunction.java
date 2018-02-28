package source.render.shader.material;

import source.util.dynamics.MaterialParameter;

import java.util.List;

/**
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class MaterialFunction {

    private String name;

    private String expression;

    private List<MaterialParameter> parameters;

    public MaterialFunction(String name) {
        this.name = name;
        this.expression = "";
    }

    public String getExpression() {
        return expression;
    }

    public void buildExpression(List<MaterialParameter> parameters, MaterialParameter returnsTo,
                                List<String> expressions) {
        this.parameters = parameters;
        StringBuilder function = new StringBuilder();
        function.append(Material.getType(returnsTo));
        function.append(" ");
        function.append(name);
        function.append("(");
        if (parameters != null) {
            List<MaterialParameter> params = parameters;
            int i = 0;
            for (MaterialParameter materialParam1 : params) {
                if (materialParam1 == null) continue;
                function.append(Material.getType(materialParam1));
                function.append(" ");
                function.append(materialParam1.getIdentifier());
                if (i++ != params.size() - 1) function.append(", ");
            }
        }
        function.append(") {\n");
        if (expressions != null) {
            for (String line : expressions) {
                function.append("    ");
                function.append(line);
                function.append("\n");
            }
        }
        function.append("}\n");

        this.expression = function.toString();
        //TODO: replace List<Param> value mapping from outputs, replace with (pre-built) MaterialFunction
        //TODO: create global MaterialFunction pre built library (written to files for memory, index functions)
    }

    public String getName() {
        return name;
    }

    public List<MaterialParameter> getParameters() {
        return parameters;
    }
}
