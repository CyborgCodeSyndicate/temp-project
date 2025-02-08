package com.example.project;


import com.example.project.base.World;
import com.example.project.ui.elements.ZeroBank.*;
import com.theairebellion.zeus.framework.base.BaseTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.ui.annotations.UI;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;

import static javax.swing.text.html.HTML.Tag;


@UI
public class ZeroBankTest extends BaseTest {


    @Test()
    @Description("COMPONENTS: Button, Input, Link, Select, Alert")
    public void alertTest(Quest quest) {
        quest
                .enters(World.EARTH)
                .navigate("http://zero.webappsecurity.com/")
                .button().click(ButtonFields.SIGN_IN_BUTTON)
                .input().insert(InputFields.USERNAME_FIELD, "username")
                .input().insert(InputFields.PASSWORD_FIELD, "password")
                .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
                .back()
                .link().click(LinkFields.TRANSFER_FUNDS_LINK)
                .select().selectOption(SelectFields.TF_FROM_ACCOUNT_DDL, "Credit Card(Avail. balance = $ -265)")
                .select().selectOption(SelectFields.TF_TO_ACCOUNT_DDL, "Loan(Avail. balance = $ 780)")
                .input().insert(InputFields.AMOUNT_FIELD, "100")
                .input().insert(InputFields.TF_DESCRIPTION_FIELD, "IDEMO BRE")
                .button().click(ButtonFields.SUBMIT_BUTTON)
                .button().click(ButtonFields.SUBMIT_BUTTON)
                .alert().validateValue(AlertFields.SUBMITTED_TRANSACTION, "You successfully submitted your transaction.")
                .complete();
    }

    @Test
    @Description("COMPONENTS: Button, Input, Link, List, Select, Radio, Alert")
    public void validatePurchaseCheckRadioButtons(Quest quest) {
        quest
                .enters(World.EARTH)
                .navigate("http://zero.webappsecurity.com/")
                .button().click(ButtonFields.SIGN_IN_BUTTON)
                .input().insert(InputFields.USERNAME_FIELD, "username")
                .input().insert(InputFields.PASSWORD_FIELD, "password")
                .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
                .back()
                .link().click(LinkFields.TRANSFER_FUNDS_LINK)
                .list().select(ListFields.NAVIGATION_TABS, "Pay Bills")
                .list().select(ListFields.PAY_BILLS_TABS, "Purchase Foreign Currency")
                .select().selectOption(SelectFields.PC_CURRENCY_DDL, "Mexico (peso)")
                .input().insert(InputFields.AMOUNT_CURRENCY_FIELD, "100")
                .radio().select(RadioFields.DOLLARS_RADIO_FIELD)
                .button().click(ButtonFields.CALCULATE_COST_BUTTON)
                .button().click(ButtonFields.PURCHASE_BUTTON)
                .alert().validateValue(AlertFields.FOREIGN_CURRENCY_CASH, "Foreign currency cash was successfully purchased.")
                .complete();
    }

    @Test
    @Description("COMPONENTS: Button, Input, Link, List, Validate, Select, Alert")
    public void paragraphTextValueTestSoftAssertionsFailedTest(Quest quest) {
        quest
                .enters(World.EARTH)
                .navigate("http://zero.webappsecurity.com/")
                .button().click(ButtonFields.SIGN_IN_BUTTON)
                .input().insert(InputFields.USERNAME_FIELD, "username")
                .input().insert(InputFields.PASSWORD_FIELD, "password")
                .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
                .back()
                .link().click(LinkFields.TRANSFER_FUNDS_LINK)
                .list().select(ListFields.NAVIGATION_TABS, "Pay Bills")
                .list().select(ListFields.PAY_BILLS_TABS, "Pay Saved Payee")
                .select().selectOption(SelectFields.SP_PAYEE_DDL, "Sprint")
                .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
                .validate()
                .validateTextInField(Tag.I, "For 12119415161214 Sprint account", true)
                .select().selectOption(SelectFields.SP_PAYEE_DDL, "Bank of America")
                .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
                .validate()
                .validateTextInField(Tag.I, "For 47844181491040 Bank of America account", true)
                .select().selectOption(SelectFields.SP_PAYEE_DDL, "Apple")
                .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
                .validate()
                .validateTextInField(Tag.I, "For 48944145651315q Apple account", true)
                .select().selectOption(SelectFields.SP_PAYEE_DDL, "Wells Fargo")
                .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
                .validate()
                .validateTextInField(Tag.I, "For 98480498848942 Wells Fargo account", true)
                .select().selectOption(SelectFields.SP_ACCOUNT_DDL, "Checking")
                .input().insert(InputFields.SP_AMOUNT_FIELD, "1000")
                .input().insert(InputFields.SP_DATE_FIELD, "2025-01-27")
                .input().insert(InputFields.SP_DESCRIPTION_FIELD, "Description Example")
                .button().click(ButtonFields.PAY_BUTTON)
                .alert().validateValue(AlertFields.PAYMENT_MESSAGE, "The payment was successfully submitted.", true)
                .complete();
    }

    @Test
    @Description("COMPONENTS: Button, Input, Link, List, Validate, Select")
    public void paragraphTextValueTestAssertionsSuccess(Quest quest) {
        quest
                .enters(World.EARTH)
                .navigate("http://zero.webappsecurity.com/")
                .button().click(ButtonFields.SIGN_IN_BUTTON)
                .input().insert(InputFields.USERNAME_FIELD, "username")
                .input().insert(InputFields.PASSWORD_FIELD, "password")
                .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
                .back()
                .link().click(LinkFields.TRANSFER_FUNDS_LINK)
                .list().select(ListFields.NAVIGATION_TABS, "Pay Bills")
                .list().select(ListFields.PAY_BILLS_TABS, "Pay Saved Payee")
                .select().selectOption(SelectFields.SP_PAYEE_DDL, "Sprint")
                .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
                .validate()
                .validateTextInField(Tag.I, "For 12119415161214 Sprint account", true)
                .select().selectOption(SelectFields.SP_PAYEE_DDL, "Bank of America")
                .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
                .validate()
                .validateTextInField(Tag.I, "For 47844181491040 Bank of America account")
                .select().selectOption(SelectFields.SP_PAYEE_DDL, "Apple")
                .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
                .validate()
                .validateTextInField(Tag.I, "For 48944145651315 Apple account", false)
                .complete();
    }

    @Test
    @Description("COMPONENTS: Button, Input, Link, List, Validate, Select")
    public void paragraphTextValueTestHardAssertionsFailedTest(Quest quest) {
        quest
                .enters(World.EARTH)
                .navigate("http://zero.webappsecurity.com/")
                .button().click(ButtonFields.SIGN_IN_BUTTON)
                .input().insert(InputFields.USERNAME_FIELD, "username")
                .input().insert(InputFields.PASSWORD_FIELD, "password")
                .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
                .back()
                .link().click(LinkFields.TRANSFER_FUNDS_LINK)
                .list().select(ListFields.NAVIGATION_TABS, "Pay Bills")
                .list().select(ListFields.PAY_BILLS_TABS, "Pay Saved Payee")
                .select().selectOption(SelectFields.SP_PAYEE_DDL, "Sprint")
                .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
                .validate()
                .validateTextInField(Tag.I, "For 12119415161214 Sprint account")
                .select().selectOption(SelectFields.SP_PAYEE_DDL, "Bank of America")
                .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
                .validate()
                .validateTextInField(Tag.I, "For 47844181491040 Bank of America account", false)
                .select().selectOption(SelectFields.SP_PAYEE_DDL, "Apple")
                .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
                .validate()
                .validateTextInField(Tag.I, "For 48944145651315q Apple account")
                .select().selectOption(SelectFields.SP_PAYEE_DDL, "Wells Fargo")
                .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
                .validate()
                .validateTextInField(Tag.I, "For 98480498848942 Wells Fargo account", false)
                .complete();
    }

    @Test
    @Description("COMPONENTS: Button, Input, Link, List, Validate, Select")
    public void paragraphTextValueTestMixedAssertionsFailedTest(Quest quest) {
        quest
                .enters(World.EARTH)
                .navigate("http://zero.webappsecurity.com/")
                .button().click(ButtonFields.SIGN_IN_BUTTON)
                .input().insert(InputFields.USERNAME_FIELD, "username")
                .input().insert(InputFields.PASSWORD_FIELD, "password")
                .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
                .back()
                .link().click(LinkFields.TRANSFER_FUNDS_LINK)
                .list().select(ListFields.NAVIGATION_TABS, "Pay Bills")
                .list().select(ListFields.PAY_BILLS_TABS, "Pay Saved Payee")
                .select().selectOption(SelectFields.SP_PAYEE_DDL, "Sprint")
                .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
                .validate()
                .validateTextInField(Tag.I, "For 12119415161214 Sprint account")
                .select().selectOption(SelectFields.SP_PAYEE_DDL, "Bank of America")
                .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
                .validate()
                .validateTextInField(Tag.I, "For 47844181491040 Bank of America account", false)
                .select().selectOption(SelectFields.SP_PAYEE_DDL, "Apple")
                .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
                .validate().validateTextInField(Tag.I, "For 48944145651315q Apple account", true)
                .select().selectOption(SelectFields.SP_PAYEE_DDL, "Wells Fargo")
                .link().click(LinkFields.SP_PAYEE_DETAILS_LINK)
                .validate()
                .validateTextInField(Tag.I, "For 98480498848942 Wells Fargo account")
                .complete();
    }

    @Test
    @Description("COMPONENTS: Button, Input, Link, List, Validate, Select")
    public void tableAssert(Quest quest) {
        quest
                .enters(World.EARTH)
                .navigate("http://zero.webappsecurity.com/")
                .button().click(ButtonFields.SIGN_IN_BUTTON)
                .input().insert(InputFields.USERNAME_FIELD, "username")
                .input().insert(InputFields.PASSWORD_FIELD, "password")
                .button().click(ButtonFields.SIGN_IN_FORM_BUTTON)
                .back()
                .button().click(ButtonFields.MORE_SERVICES_BUTTON)
                .link().click(LinkFields.ACCOUNT_ACTIVITY_LINK)
                .list().select(ListFields.ACCOUNT_ACTIVITY_TABS, "Find Transactions")
                .input().insert(InputFields.AA_DESCRIPTION_FIELD, "ONLINE")
                .input().insert(InputFields.AA_FROM_DATE_FIELD, "2012-01-01")
                .input().insert(InputFields.AA_TO_DATE_FIELD, "2012-12-31")
                .input().insert(InputFields.AA_FROM_AMOUNT_FIELD, "100")
                .input().insert(InputFields.AA_TO_AMOUNT_FIELD, "1000")
                .select().selectOption(SelectFields.AA_TYPE_DDL, "Deposit")
                .button().click(ButtonFields.FIND_SUBMIT_BUTTON)
// TODO: test table impl
//                .table().getValue()
                .complete();
    }

}
