package pl.eiti.bpelag.ui.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpel.model.Assign;
import org.eclipse.bpel.model.Copy;
import org.eclipse.bpel.model.From;
import org.eclipse.bpel.model.impl.AssignImpl;
import org.eclipse.bpel.model.impl.CopyImpl;
import org.eclipse.bpel.ui.adapters.IVirtualCopyRuleSide;
import org.eclipse.bpel.ui.util.BPELUtil;
import org.eclipse.emf.ecore.EObject;

import pl.eiti.bpelag.util.Messages;

public class AnalyzerWizardModel {
	private List<Assign> assignList = null;
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

	public List<String> getCopyList(int index) {
		List<String> copies = new ArrayList<>();
		AssignImpl selected = (AssignImpl) assignList.get(index);

		for (Copy copy : selected.getCopy()) {
			if (copy instanceof CopyImpl) {
				copies.add(new String(asText(copy.getFrom()) + " to " + asText(copy.getTo())));
			}
		}

		return copies;
	}

	private String asText(EObject elem) {
		IVirtualCopyRuleSide side = BPELUtil.adapt(elem, IVirtualCopyRuleSide.class);
		String result = "?";

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

}
