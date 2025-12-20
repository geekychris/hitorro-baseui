/*
 * Copyright (c) 2006-2025 Chris Collins
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.hitorro.baseui.pages.test;


import com.hitorro.analysis.rdf.JenaModelCacheMapper;
import com.hitorro.util.core.Log;
import com.hitorro.util.core.string.StringUtil;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.tapestry5.annotations.Persist;

import java.io.ByteArrayOutputStream;

/*
bloggers

 PREFIX foaf: <http://xmlns.com/foaf/0.1/>
SELECT ?name ?url
FROM 	<bloggers.rdf>
WHERE  {
    ?x foaf:name ?name .
    ?x foaf:weblog ?url .
}


 */
public class Sparql {
    @Persist
    private String sparqlText;

    @Persist
    private String datasetText;

    private String output;

    public String getSparqlText() {
        return sparqlText;
    }

    public void setSparqlText(String text) {
        this.sparqlText = text;
    }

    public String getOutput() {
        return output;
    }

    public String getDatasetText() {
        return datasetText;
    }

    public void setDatasetText(String text) {
        this.datasetText = text;
    }

    void onActivate() {
        try {
            if (!StringUtil.nullOrEmptyOrBlankString(sparqlText) && !StringUtil.nullOrEmptyOrBlankString(datasetText)) {
                Model model = JenaModelCacheMapper.instance.get(datasetText);

                QueryExecution qe = QueryExecutionFactory.create(sparqlText, model);
                ResultSet results = qe.execSelect();

                // Output query results
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ResultSetFormatter.out(baos, results);
                output = baos.toString();


                // Important - free up resources used running the query
                qe.close();
            }
        } catch (Exception e) {
            Log.util.error("%e", e);
        }


    }
}
