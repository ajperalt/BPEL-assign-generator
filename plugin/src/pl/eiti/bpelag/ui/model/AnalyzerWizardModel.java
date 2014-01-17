package pl.eiti.bpelag.ui.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpel.model.Assign;
import org.eclipse.bpel.model.Copy;
import org.eclipse.bpel.model.From;
import org.eclipse.bpel.model.To;
import org.eclipse.bpel.model.Variable;
import org.eclipse.bpel.model.impl.AssignImpl;
import org.eclipse.bpel.model.impl.CopyImpl;
import org.eclipse.bpel.model.impl.FromImpl;
import org.eclipse.bpel.model.impl.ToImpl;
import org.eclipse.bpel.ui.adapters.IVirtualCopyRuleSide;
import org.eclipse.bpel.ui.util.BPELUtil;
import org.eclipse.emf.ecore.EObject;

import pl.eiti.bpelag.util.Messages;

public class AnalyzerWizardModel {
	private List<Variable> processVariables = null;
	private List<Assign> assignList = null;
	private Assign currentlyProcessingAssign = null;

	private List<Copy> copyList = null;
	private Copy currentlyProcessingCopy = null;
	private List<String> markers = null;

	private List<String> toComboList = null;
	private List<String> fromComboList = null;

	public AnalyzerWizardModel() {
		assignList = new ArrayList<Assign>();

		toComboList = new ArrayList<>();

		toComboList.add(Messages.ASSIGN_CATEGORY_VARPART);
		toComboList.add(Messages.ASSIGN_CATEGORY_EXPRESSION);
		toComboList.add(Messages.ASSIGN_CATEGORY_VARPROPERTY);
		toComboList.add(Messages.ASSIGN_CATEGORY_PARTNERROLE);

		fromComboList = new ArrayList<>();

		fromComboList.add(Messages.ASSIGN_CATEGORY_VARPART);
		fromComboList.add(Messages.ASSIGN_CATEGORY_EXPRESSION);
		fromComboList.add(Messages.ASSIGN_CATEGORY_LITERAL);
		fromComboList.add(Messages.ASSIGN_CATEGORY_VARPROPERTY);
		fromComboList.add(Messages.ASSIGN_CATEGORY_PARTNERROLE);
		fromComboList.add(Messages.ASSIGN_CATEGORY_ENDPOINTREF);
		fromComboList.add(Messages.ASSIGN_CATEGORY_OPAQUE);
	}

	public List<Assign> getAssignList() {
		return assignList;
	}

	public void setAssignList(List<Assign> assigns) {
		assignList = assigns;
	}

	public List<String> getToComboList() {
		return toComboList;
	}

	public List<String> getFromComboList() {
		return fromComboList;
	}

	public List<String> getAssignNameList() {
		List<String> names = new ArrayList<>();

		for (Assign act : assignList) {
			names.add(act.getName());
		}
		return names;
	}

	public List<String> getCopyListNames(int index) {
		List<String> copies = new ArrayList<>();
		copyList = new ArrayList<>();
		currentlyProcessingAssign = (AssignImpl) assignList.get(index);

		for (Copy copy : currentlyProcessingAssign.getCopy()) {
			if (copy instanceof CopyImpl) {
				copyList.add(copy);
				copies.add(new String(asText(copy.getFrom()) + " to " + asText(copy.getTo())));
			}
		}

		return copies;
	}

	public void setCurrentlyProcessingCopy(int index) {
		currentlyProcessingCopy = copyList.get(index);
	}

	public List<Copy> getCopyList() {
		return copyList;
	}

	public void setCopyList(List<Copy> copyList) {
		this.copyList = copyList;
	}

	public void setToComboList(List<String> toComboList) {
		this.toComboList = toComboList;
	}

	public void setFromComboList(List<String> fromComboList) {
		this.fromComboList = fromComboList;
	}

	private String asText(EObject elem) {
		IVirtualCopyRuleSide side = BPELUtil.adapt(elem, IVirtualCopyRuleSide.class);
		String result = "?";

		if (null == side) {
			return result;
		}

		if (null != side.getVariable()) {
			if (null != side.getProperty()) {
				result = Messages.ASSIGN_CATEGORY_VARPROPERTY;
			} else {
				result = Messages.ASSIGN_CATEGORY_VARPART;
			}
		} else if (null != side.getExpression()) {
			result = Messages.ASSIGN_CATEGORY_EXPRESSION;
		} else if (null != side.getPartnerLink()) {
			result = Messages.ASSIGN_CATEGORY_PARTNERROLE;
		} else {
			From from = BPELUtil.adapt(elem, From.class);
			if (null != from.getServiceRef()) {
				result = Messages.ASSIGN_CATEGORY_ENDPOINTREF;
			} else if (null != from.getLiteral()) {
				result = Messages.ASSIGN_CATEGORY_LITERAL;
			} else if (Boolean.TRUE.equals(from.getOpaque())) {
				result = Messages.ASSIGN_CATEGORY_OPAQUE;
			}
		}

		return result;
	}

	public Integer getFromTypeCopyIndex(Integer copySelectionIndex) {
		if (null == currentlyProcessingCopy || !copyList.get(copySelectionIndex).equals(currentlyProcessingCopy)) {
			currentlyProcessingCopy = copyList.get(copySelectionIndex);
		}
		return fromComboList.indexOf(asText(currentlyProcessingCopy.getFrom()));
	}

	public Integer getToTypeCopyIndex(Integer copySelectionIndex) {
		if (null == currentlyProcessingCopy || !copyList.get(copySelectionIndex).equals(currentlyProcessingCopy)) {
			currentlyProcessingCopy = copyList.get(copySelectionIndex);
		}
		return toComboList.indexOf(asText(currentlyProcessingCopy.getTo()));
	}

	public List<Variable> getProcessVariables() {
		return processVariables;
	}

	public void setProcessVariables(List<Variable> processVariables) {
		this.processVariables = processVariables;
	}

	public Assign getCurrentlyProcessingAssign() {
		return currentlyProcessingAssign;
	}

	public void setCurrentlyProcessingAssign(Assign currentlyProcessingAssign) {
		this.currentlyProcessingAssign = currentlyProcessingAssign;
	}

	public Copy getCurrentlyProcessingCopy() {
		return currentlyProcessingCopy;
	}

	public void setCurrentlyProcessingCopy(Copy currentlyProcessingCopy) {
		this.currentlyProcessingCopy = currentlyProcessingCopy;
	}

	public Integer getFromVarIndex() {
		Integer result = null;
		if (null != currentlyProcessingCopy && null != currentlyProcessingCopy.getFrom()) {
			result = processVariables.indexOf(currentlyProcessingCopy.getFrom().getVariable());
		}
		return result;
	}

	public Integer getToVarIndex() {
		Integer result = null;
		if (null != currentlyProcessingCopy && null != currentlyProcessingCopy.getTo()) {
			result = processVariables.indexOf(currentlyProcessingCopy.getTo().getVariable());
		}
		return result;
	}

	public String getFromPartName() {
		String result = null;

		From currentFrom = currentlyProcessingCopy.getFrom();
		if (currentFrom instanceof FromImpl) {
		}
		return result;
	}

	public String getToPartName() {
		String result = null;

		To currentTo = currentlyProcessingCopy.getTo();
		if (currentTo instanceof ToImpl) {
		}
		return result;
	}

	public String getElementNameFrom() {
		return currentlyProcessingCopy.getFrom().getQuery().getValue();
	}

	public String getElementNameTo() {
		return currentlyProcessingCopy.getTo().getQuery().getValue();
	}

	public List<String> getMarkers() {
		return markers;
	}

	public void setMarkers(List<String> markers) {
		this.markers = markers;
	}
}
