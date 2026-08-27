package com.diana.auditinsightbackendspringboot.Services;

import com.diana.auditinsightbackendspringboot.Enum.OrganisationType;

import java.util.Map;
import java.util.Set;

public final class EvidenceFolderValidator {

    private EvidenceFolderValidator() {}

    private static final Map<String, Set<String>> PRIVATE_ALLOWED = Map.ofEntries(
            Map.entry("Financial Reporting",
                    Set.of(
                            "General Ledgers",
                            "Trial Balances",
                            "Financial Statements",
                            "Management Accounts",
                            "Journal Entries",
                            "Journal Vouchers",
                            "Supporting Schedules",
                            "Monthly Financial Reports"
                    )),

            Map.entry("Sales and Revenue",
                    Set.of(
                            "Sales Invoices",
                            "Receipts",
                            "Credit Notes",
                            "Sales Orders",
                            "Customer Contracts",
                            "Delivery Notes"
                    )),

            Map.entry("Banking and Cash",
                    Set.of(
                            "Bank Statements",
                            "Bank Reconciliations",
                            "Cashbooks",
                            "Petty Cash Records",
                            "Cash Count Sheets",
                            "Payment Confirmations",
                            "Cheque Copies"
                    )),

            Map.entry("Purchases and Procurement",
                    Set.of(
                            "Purchase Requisitions",
                            "Purchase Orders",
                            "Supplier Quotations",
                            "Supplier Invoices",
                            "Goods Received Notes",
                            "Supplier Contracts"
                    )),

            Map.entry("Accounts Receivable and Payable",
                    Set.of(
                            "Customer Statements",
                            "Supplier Statements",
                            "Accounts Receivable Aging",
                            "Accounts Payable Aging",
                            "Customer Confirmations",
                            "Supplier Confirmations",
                            "Receivables Reconciliations",
                            "Payables Reconciliations",
                            "Bad Debt Provisions"
                    )),

            Map.entry("Payment Evidence",
                    Set.of(
                            "Payment Vouchers",
                            "Payment Requests",
                            "Payment Approval Forms",
                            "Electronic Transfer Confirmations",
                            "Mobile Money Confirmations",
                            "Cheque Copies",
                            "Payment Schedules"
                    )),

            Map.entry("Payroll and HR",
                    Set.of(
                            "Payroll Registers",
                            "Employment Contracts",
                            "Timesheets",
                            "Leave Records",
                            "Staff Lists",
                            "PAYE Records",
                            "RSSB Contributions"
                    )),

            Map.entry("Tax and Compliance",
                    Set.of(
                            "VAT Returns",
                            "PAYE Filings",
                            "Corporate Income Tax Returns",
                            "Withholding Tax Records",
                            "Tax Clearance Certificates",
                            "Tax Assessments",
                            "RRA Correspondence"
                    )),

            Map.entry("Inventory and Assets",
                    Set.of(
                            "Inventory Registers",
                            "Stock Count Sheets",
                            "Goods Received Notes",
                            "Asset Registers",
                            "Asset Purchase Documents",
                            "Asset Disposal Documents",
                            "Depreciation Schedules",
                            "Physical Verification Reports"
                    )),

            Map.entry("Loans and Financing",
                    Set.of(
                            "Loan Agreements",
                            "Bank Loan Statements",
                            "Loan Repayment Schedules",
                            "Interest Schedules",
                            "Guarantees and Securities",
                            "Shareholder Loan Agreements",
                            "Financing Correspondence"
                    )),

            Map.entry("Legal and Governance",
                    Set.of(
                            "Company Registration Documents",
                            "Shareholder Documents",
                            "Board Minutes",
                            "Management Meeting Minutes",
                            "Company Policies",
                            "Contracts",
                            "Licenses and Permits",
                            "Insurance Documents"
                    )),

            Map.entry("IT and System Evidence",
                    Set.of(
                            "Access Logs",
                            "Audit Trail Exports",
                            "Backup Reports",
                            "System Reports",
                            "User Access Records"
                    )),

            Map.entry("Audit and Review",
                    Set.of(
                            "Audit Requests",
                            "Audit Reports",
                            "Management Letters",
                            "Management Responses",
                            "Corrective Action Plans",
                            "Internal Audit Reports",
                            "Review Working Papers"
                    )),

            Map.entry("Other Supporting Documents",
                    Set.of(
                            "Emails",
                            "Screenshots",
                            "Approval Documents",
                            "Correspondence",
                            "Miscellaneous"
                    ))

    );

    private static final Map<String, Set<String>> NGO_ALLOWED = Map.ofEntries(
            Map.entry("Financial Reporting",
                    Set.of(
                            "General Ledgers",
                            "Trial Balances",
                            "Financial Statements",
                            "Project Financial Reports",
                            "Donor Financial Reports",
                            "Management Accounts",
                            "Journal Entries",
                            "Supporting Schedules"
                    )),

            Map.entry("Budget Management",
                    Set.of(
                            "Approved Annual Budget",
                            "Project Budgets",
                            "Grant Budgets",
                            "Budget Revisions",
                            "Budget vs Actual Reports",
                            "Budget Approval Minutes"
                    )),

            Map.entry("Banking and Cash",
                    Set.of(
                            "Bank Statements",
                            "Bank Reconciliations",
                            "Payment Confirmations",
                            "Cashbooks",
                            "Cash Count Sheets",
                            "Petty Cash Vouchers",
                            "Cheque Copies"
                    )),

            Map.entry("Payment Evidence",
                    Set.of(
                            "Payment Vouchers",
                            "Signed Payment Requests",
                            "Electronic Transfer Confirmations",
                            "Cheque Copies",
                            "Mobile Money Confirmations",
                            "Payment Approval Forms",
                            "Payment Schedules"
                    )),

            Map.entry("Receivables, Payables and Advances",
                    Set.of(
                            "Accounts Receivable Aging",
                            "Accounts Payable Aging",
                            "Customer and Debtor Statements",
                            "Supplier Statements",
                            "Receivables Reconciliations",
                            "Payables Reconciliations",
                            "Staff Advances",
                            "Staff Advance Liquidations",
                            "Partner Advances",
                            "Advance Reconciliations",
                            "Balance Confirmations"
                    )),

            Map.entry("Grants and Donor Agreements",
                    Set.of(
                            "Grant Agreements",
                            "Funding Agreements",
                            "Donor Contracts",
                            "Grant Amendments",
                            "Donor Correspondence"
                    )),

            Map.entry("Grant and Donor Reconciliations",
                    Set.of(
                            "Grant Reconciliations",
                            "Donor Fund Balances",
                            "Restricted Fund Schedules",
                            "Unrestricted Fund Schedules",
                            "Grant Expenditure Schedules",
                            "Grant Advance Reconciliations",
                            "Budget vs Actual Reconciliations"
                    )),

            Map.entry("Donor Compliance",
                    Set.of(
                            "Donor Guidelines",
                            "Reporting Requirements",
                            "Compliance Checklists",
                            "Donor Approvals",
                            "Waivers",
                            "Donor Monitoring Reports"
                    )),

            Map.entry("Implementing Partners and Sub-Grants",
                    Set.of(
                            "Partner Agreements",
                            "Sub-Grant Agreements",
                            "Partner Budgets",
                            "Partner Financial Reports",
                            "Partner Narrative Reports",
                            "Partner Monitoring Reports",
                            "Partner Due Diligence Assessments",
                            "Partner Audit Reports",
                            "Partner Advance Reports",
                            "Partner Liquidation Reports"
                    )),

            Map.entry("Project Documentation",
                    Set.of(
                            "Project Proposals",
                            "Work Plans",
                            "Activity Reports",
                            "Project Completion Reports",
                            "Monitoring Reports"
                    )),

            Map.entry("Project Activities",
                    Set.of(
                            "Training Reports",
                            "Workshop Reports",
                            "Workshop Agendas",
                            "Workshop Attendance Lists",
                            "Signed Attendance Sheets",
                            "Meeting Minutes",
                            "Evaluation Forms",
                            "Photographs"
                    )),

            Map.entry("Beneficiary Documentation",
                    Set.of(
                            "Beneficiary Lists",
                            "Beneficiary Registration Forms",
                            "Beneficiary IDs",
                            "Distribution Lists",
                            "Acknowledgement Receipts",
                            "Consent Forms"
                    )),

            Map.entry("Procurement",
                    Set.of(
                            "Purchase Requisitions",
                            "Procurement Plans",
                            "Purchase Orders",
                            "Supplier Quotations",
                            "Tender Documents",
                            "Bid Opening Minutes",
                            "Bid Evaluation Reports",
                            "Supplier Selection Approvals",
                            "Supplier Invoices",
                            "Goods Received Notes",
                            "Delivery Notes",
                            "Supplier Contracts",
                            "Procurement Waivers"
                    )),

            Map.entry("Payroll and HR",
                    Set.of(
                            "Payroll Registers",
                            "Employment Contracts",
                            "Timesheets",
                            "Leave Records",
                            "Staff Lists",
                            "Performance Contracts",
                            "PAYE Records",
                            "RSSB Contributions",
                            "Salary Approval Letters",
                            "Employee Expense Claims",
                            "HR Policies"
                    )),

            Map.entry("Travel",
                    Set.of(
                            "Travel Authorizations",
                            "Travel Expense Claims",
                            "Flight Tickets",
                            "Hotel Invoices",
                            "Travel Reports",
                            "Boarding Passes"
                    )),

            Map.entry("Vehicles",
                    Set.of(
                            "Vehicle Logbooks",
                            "Fuel Records",
                            "Vehicle Maintenance Records",
                            "Vehicle Insurance",
                            "Vehicle Allocation Records"
                    )),

            Map.entry("Fixed Assets",
                    Set.of(
                            "Asset Register",
                            "Asset Tags",
                            "Purchase Documents",
                            "Asset Transfer Forms",
                            "Asset Disposal Forms",
                            "Physical Verification Reports",
                            "Maintenance Records",
                            "Depreciation Schedules"
                    )),

            Map.entry("Inventory and Distributions",
                    Set.of(
                            "Inventory Registers",
                            "Stock Count Sheets",
                            "Goods Received Notes",
                            "Warehouse Records",
                            "Stock Movement Reports",
                            "Distribution Plans",
                            "Distribution Lists",
                            "Beneficiary Acknowledgement Receipts",
                            "Damaged and Expired Stock Reports"
                    )),

            Map.entry("Internal Controls and Risk Management",
                    Set.of(
                            "Internal Control Policies",
                            "Risk Registers",
                            "Risk Assessments",
                            "Internal Audit Reports",
                            "Internal Audit Plans",
                            "Fraud Reports",
                            "Whistleblowing Reports",
                            "Conflict of Interest Declarations",
                            "Control Self-Assessments"
                    )),

            Map.entry("Insurance and Liabilities",
                    Set.of(
                            "Insurance Policies",
                            "Insurance Claims",
                            "Lease Agreements",
                            "Legal Claims",
                            "Provisions",
                            "Contingent Liabilities",
                            "Guarantees and Commitments"
                    )),

            Map.entry("Compliance and Tax",
                    Set.of(
                            "VAT Documents",
                            "PAYE Filings",
                            "RSSB Contributions",
                            "Tax Clearance Certificates",
                            "NGO Registration Certificates"
                    )),

            Map.entry("Legal and Governance",
                    Set.of(
                            "Board Minutes",
                            "Management Meeting Minutes",
                            "Policies",
                            "Memorandums of Understanding",
                            "Contracts",
                            "Registration Documents"
                    )),

            Map.entry("Related Parties and Declarations",
                    Set.of(
                            "Related Party Transactions",
                            "Conflict of Interest Declarations",
                            "Board Member Declarations",
                            "Management Declarations",
                            "Related Party Confirmations"
                    )),

            Map.entry("Audit Evidence",
                    Set.of(
                            "Audit Requests",
                            "Management Responses",
                            "Audit Reports",
                            "Management Letters",
                            "Corrective Action Plans",
                            "Internal Audit Reports",
                            "Review Working Papers"
                    )),

            Map.entry("IT and System Evidence",
                    Set.of(
                            "Access Logs",
                            "Audit Trail Exports",
                            "Backup Reports",
                            "System Reports",
                            "User Access Records"
                    )),

            Map.entry("Other Supporting Documents",
                    Set.of(
                            "Emails",
                            "Approval Letters",
                            "Correspondence",
                            "Miscellaneous"
                    ))


    );

    private static final Map<OrganisationType, Map<String, Set<String>>> BY_ORGANISATION_TYPE = Map.of(
            OrganisationType.PRIVATE, PRIVATE_ALLOWED,
            OrganisationType.NGO, NGO_ALLOWED
    );

    public static boolean isValid(OrganisationType organisationType, String folder, String subfolder) {
        Map<String, Set<String>> allowed = BY_ORGANISATION_TYPE.get(organisationType);
        if (allowed == null) {
            return false;
        }
        Set<String> subfolders = allowed.get(folder);
        return subfolders != null && subfolders.contains(subfolder);
    }

    public static Map<String, Set<String>> getAllowed(OrganisationType organisationType) {
        return BY_ORGANISATION_TYPE.get(organisationType);
    }
}