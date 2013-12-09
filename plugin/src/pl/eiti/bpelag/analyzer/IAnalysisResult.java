package pl.eiti.bpelag.analyzer;

import java.util.List;
import java.util.Map;

import org.eclipse.bpel.model.Assign;
import org.eclipse.bpel.model.Copy;

public interface IAnalysisResult extends Map<Assign, List<Copy>> {

}
