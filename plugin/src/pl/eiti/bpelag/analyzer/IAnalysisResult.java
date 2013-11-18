package pl.eiti.bpelag.analyzer;

import java.util.List;
import java.util.Map;

import org.eclipse.bpel.model.Assign;

import pl.eiti.bpelag.analyzer.impl.AssignElem;

public interface IAnalysisResult extends Map<Assign, List<AssignElem>> {

}
