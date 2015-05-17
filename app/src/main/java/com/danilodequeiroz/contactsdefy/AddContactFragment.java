package com.danilodequeiroz.contactsdefy;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.danilodequeiroz.contactsdefy.common.MaskedWatcher;
import com.danilodequeiroz.contactsdefy.db.DatabaseAdapter;
import com.danilodequeiroz.contactsdefy.db.DatabaseHelper;
import com.danilodequeiroz.contactsdefy.model.DefyContact;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * { @ link AddContactFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddContactFragment extends Fragment implements  DatePickerDialog.OnDateSetListener{
    private boolean EDIT_MODE = false; // Validator.ValidationListener ,
//    @Order(0)
//    @NotEmpty(sequence = 0,message = "Campo obrigat�rio.")
    public MaterialEditText mEdtName;
//    @Order(1)
    public MaterialEditText mEdtSurName;
//    @Order(2)
    public MaterialEditText mEdtBirth;
//    @Order(3)
    public MaterialEditText mEdtCompany;
//    @Order(4)
//    @NotEmpty(sequence = 1,message = "Campo obrigat�rio.")
    public MaterialEditText mEdtPhone;
    @Order(5)
    public MaterialEditText mEdtMobile;
    @Order(6)
    public MaterialEditText mEdtWorkPhone;
    private int touches = 0;
    private DefyContact defy;

    public static final String DATEPICKER_TAG = "datepicker";
    final Calendar calendar = Calendar.getInstance();
    final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), true);

    private View.OnTouchListener touchDate = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                //touches++;
                //if (touches >=2 || medtData.getError()!=null) {
                createDatePicker();
                touches = 0;
                return true;
            }
            return false;
        }
    };
    private View.OnClickListener clickDate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            createDatePicker();
        }
    };


    private void createDatePicker() {
        datePickerDialog.show(getFragmentManager(), DATEPICKER_TAG);
    }

    View.OnFocusChangeListener focusDate  = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                mEdtBirth.setOnTouchListener(touchDate);
                createDatePicker();
            }else{
                mEdtBirth.setOnTouchListener(null);
            }
        }
    };


    public AddContactFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }


    public static AddContactFragment newInstance(Long itemId) {
        AddContactFragment fragment = new AddContactFragment();
        Bundle args = new Bundle();
        args.putLong("contactId", itemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
//        mValidator = new Validator(activity);
//        mValidator.setValidationListener((Validator.ValidationListener)this);
//        this.regi
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        mValidator = new Validator(this);
        long contactId = getArguments().getLong("contactId");
        EDIT_MODE = contactId >= 0  ? true:false;
        View view = inflater.inflate(R.layout.fragment_add_contact, container, false);
        mEdtName = (MaterialEditText)view.findViewById(R.id.add_contact_edt_name);
        mEdtSurName = (MaterialEditText)view.findViewById(R.id.add_contact_edt_surname);
        mEdtBirth = (MaterialEditText)view.findViewById(R.id.add_contact_edt_birth);
        mEdtCompany = (MaterialEditText)view.findViewById(R.id.add_contact_edt_company);
        mEdtPhone = (MaterialEditText)view.findViewById(R.id.add_contact_edt_phone);
        mEdtMobile = (MaterialEditText)view.findViewById(R.id.add_contact_edt_mobile);
        mEdtWorkPhone = (MaterialEditText)view.findViewById(R.id.add_contact_edt_workphone);
//        mEdtBirth.setOnClickListener(clickDate);
        mEdtBirth.setOnFocusChangeListener(focusDate);
        mEdtBirth.setOnTouchListener(touchDate);
//        mEdtBirth.setLongClickable(false);
        mEdtMobile.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        mEdtPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        mEdtWorkPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        if(EDIT_MODE) {
            DatabaseAdapter dba = new DatabaseAdapter(getActivity().getApplicationContext());
            defy = dba.getContact(contactId);
            mEdtName.setText(defy.getName());
            mEdtSurName.setText(defy.getmSurname());
            mEdtCompany.setText(defy.getCompanyName());
            mEdtBirth.setText(defy.getBirthDdMMyyyy());
            mEdtPhone.setText(defy.getPhone());
            mEdtWorkPhone.setText(defy.getWorkPhone());
            mEdtMobile.setText(defy.getMobilePhone());
        }
        return view;
    }

    public void setText(String name) {
        EditText view = (EditText) getView().findViewById(R.id.add_contact_edt_name);
        view.setText(name);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_contacts, menu);
        if(EDIT_MODE){
            menu.setGroupVisible(R.id.add_menu, false);
            menu.setGroupVisible(R.id.list_menu, false);
//            menu.setGroupVisible(R.id.edit_menu, true);
        }else{
//            menu.setGroupVisible(R.id.add_menu, true);
            menu.setGroupVisible(R.id.list_menu, false);
            menu.setGroupVisible(R.id.edit_menu, false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DatabaseAdapter dba = new DatabaseAdapter(getActivity().getApplicationContext());
        switch (item.getItemId()){
            case R.id.save_new__contact:
                if (!validateFirstName(mEdtName.getText().toString())){
                    Crouton.makeText(getActivity(),"Digite um nome.",Style.ALERT).show();
                    return false;
                }
                ArrayList<Boolean> test = new ArrayList<>();
                test.add(PhoneNumberUtils.isGlobalPhoneNumber(mEdtPhone.getText().toString().replaceAll("[\\D]", "")));
                test.add(PhoneNumberUtils.isGlobalPhoneNumber(mEdtMobile.getText().toString().replaceAll("[\\D]", "")));
                test.add(PhoneNumberUtils.isGlobalPhoneNumber(mEdtWorkPhone.getText().toString().replaceAll("[\\D]", "")));
                if(!(!mEdtPhone.getText().toString().isEmpty() || !mEdtMobile.getText().toString().isEmpty() || !mEdtWorkPhone.getText().toString().isEmpty())){
                    Crouton.makeText(getActivity(),"Digite no mínimo um telefone.",Style.ALERT).show();
                    return false;
                }
                DefyContact contact = new DefyContact();
                contact.setmName(mEdtName.getText().toString());
                contact.setBirthDdMMyyyy(mEdtBirth.getText().toString());
                contact.setmSurname(mEdtSurName.getText().toString());
                contact.setPhone(mEdtPhone.getText().toString());
                contact.setMobilePhone(mEdtMobile.getText().toString());
                contact.setWorkPhone(mEdtWorkPhone.getText().toString());
                contact.setCompanyName(mEdtCompany.getText().toString());
                dba.insert(contact);
                getFragmentManager().popBackStackImmediate();
                return true;
            case R.id.edit_save__contact:

                dba.updContact((int)defy.getmId(), DefyContact.ColummValue.NAME,mEdtName.getText().toString());
                dba.updContact((int)defy.getmId(), DefyContact.ColummValue.SURNAME,mEdtSurName.getText().toString());
                SimpleDateFormat ddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat yyyyMMdd=new SimpleDateFormat("yyyy-MM-dd");
                String formattedBirth= "";
                try {
                    formattedBirth = yyyyMMdd.format(ddMMyyyy.parse(mEdtBirth.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                };
                dba.updContact((int)defy.getmId(), DefyContact.ColummValue.BIRTHDATE,formattedBirth);
                dba.updContact((int)defy.getmId(), DefyContact.ColummValue.COMPANY,mEdtCompany.getText().toString());
                dba.updContact((int)defy.getmId(), DefyContact.ColummValue.PHONE,mEdtPhone.getText().toString());
                dba.updContact((int)defy.getmId(), DefyContact.ColummValue.MOBILEPHONE,mEdtMobile.getText().toString());
                dba.updContact((int)defy.getmId(), DefyContact.ColummValue.WORKPHONE,mEdtWorkPhone.getText().toString());
                getFragmentManager().popBackStackImmediate();
                return true;
            default:
                return false;

        }
    }


//    @Override
//    public void onValidationSucceeded() {
//
//    }
//insert

    public static boolean validatePhoneNumber(String phoneNumber) {
        String regexPhone = "^(?:(?:\\+?1\\s*(?:[.-]\\s*)?)?(?:\\(\\s*([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9])\\s*\\)|([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9]))\\s*(?:[.-]\\s*)?)?([2-9]1[02-9]|[2-9][02-9]1|[2-9][02-9]{2})\\s*(?:[.-]\\s*)?([0-9]{4})(?:\\s*(?:#|x\\.?|ext\\.?|extension)\\s*(\\d+))?$";
        Pattern pattern = Pattern.compile(regexPhone);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean validateFirstName(String firstName ) {
        return firstName.length() > 3;
    }

    public static boolean validateLastName( String lastName ) {
        return lastName.length() > 3;
    }



    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        mEdtBirth.setText(String.format("%02d", day)+"/"+String.format("%02d", month+1)+"/"+year);
    }
}
