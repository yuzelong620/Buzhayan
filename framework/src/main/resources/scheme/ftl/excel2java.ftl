package com.kratos.game.${game}.auto.server;
import java.util.List;
import com.kratos.engine.framework.scheme.core.custom.StringIntTuple;
import com.kratos.engine.framework.scheme.core.custom.IntDoubleTuple;
import com.kratos.engine.framework.scheme.core.custom.IntTuple;
import com.kratos.engine.framework.scheme.core.custom.ThreeTuple;
import com.kratos.engine.framework.scheme.core.custom.StringFloatTuple;

/**
*自动生成类
*/
public class ${className?cap_first}{
<#list fields as field>
	/** ${field.fieldDesc}*/
	private ${field.fieldType}	${field.fieldName};
</#list>

<#list fields as field>
	/** ${field.fieldDesc}*/
	public ${field.fieldType?cap_first} get${field.fieldName?cap_first}(){
		return this.${field.fieldName};
	}
</#list>
<#list fields as field>
	/**${field.fieldDesc}*/
	public void set${field.fieldName?cap_first}(${field.fieldType} ${field.fieldName}){
		this.${field.fieldName} = ${field.fieldName};
	}
</#list>
}