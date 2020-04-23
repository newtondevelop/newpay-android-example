package org.newtonproject.newtoncore.android.views.contact;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.tbruyelle.rxpermissions2.RxPermissions;

import org.newtonproject.newtoncore.android.C;
import org.newtonproject.newtoncore.android.R;
import org.newtonproject.newtoncore.android.components.IndexView;
import org.newtonproject.newtoncore.android.data.entity.common.ContactsInfo;
import org.newtonproject.newtoncore.android.data.entity.common.Wallet;
import org.newtonproject.newtoncore.android.router.AddFriendRouter;
import org.newtonproject.newtoncore.android.router.SendRouter;
import org.newtonproject.newtoncore.android.router.UpdateFriendRouter;
import org.newtonproject.newtoncore.android.utils.FileUtils;
import org.newtonproject.newtoncore.android.utils.Logger;
import org.newtonproject.newtoncore.android.utils.NewAddressUtils;
import org.newtonproject.newtoncore.android.viewmodels.addressbook.AddressBookViewModel;
import org.newtonproject.newtoncore.android.viewmodels.addressbook.AddressBookViewModelFactory;
import org.newtonproject.newtoncore.android.views.base.BaseImplActivity;
import org.newtonproject.newtoncore.android.views.widget.adapter.ContactsAdapter;
import org.newtonproject.newtoncore.android.widget.DeleteItemPopuwindow;
import org.newtonproject.newtoncore.android.widget.MorePopupWindow;
import org.newtonproject.newtoncore.android.widget.SampleItemDecoration;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import dagger.android.AndroidInjection;
import io.reactivex.disposables.Disposable;

/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class AddressBookActivity extends BaseImplActivity<AddressBookViewModel> implements View.OnClickListener {

    private static final int FILE_SELECT_CODE = 2000;
    @Inject
    public AddressBookViewModelFactory factory;

    private static final String TAG = "AddressBookActivity";
    @BindView(R.id.friendsList)
    RecyclerView friendsListView;
    @BindView(R.id.emptyRelativeLayout)
    ConstraintLayout emptyRelativeLayout;
    @BindView(R.id.addAddressBt)
    FloatingActionButton addAddressBt;
    @BindView(R.id.indexView)
    IndexView indexView;
    @BindView(R.id.frameLayout)
    FrameLayout rootView;
    @BindView(R.id.popRootView)
    FrameLayout popRootView;


    private Wallet wallet;
    private ContactsAdapter adapter;

    private AddressBookActivity addressBookActivity;

    public static int SOURCE_CODE = 0;

    public static int SOURCE_ME = 1;
    public static int SOURCE_SEND = 0;
    private RxPermissions rxPermissions;
    private ArrayList<ContactsInfo> contactsInfos;
    private MorePopupWindow morePopupWindow;
    private boolean needImport = false;
    private String needImportFile;

    @Override
    protected int getActivityTitle() {
        return R.string.text_address_book;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_friends;
    }

    @Override
    protected void initIntent() {
        SOURCE_CODE = getIntent().getIntExtra(C.FRIEND_SOURCE, 0);
        wallet = getIntent().getParcelableExtra(C.Key.WALLET);
        Uri data = getIntent().getData();
        if(data != null) {
            needImport = true;
            needImportFile = FileUtils.getPath(mContext, data);
        }
    }

    @Override
    protected void initViewModel() {
        mViewModel = ViewModelProviders.of(this, factory).get(AddressBookViewModel.class);
        mViewModel.onDeleteContact().observe(this, this::onDeleteContact);
        mViewModel.onContactInfo().observe(this, this::onContactInfo);
        mViewModel.onExportContact().observe(this, this::onExportContact);
        mViewModel.onImportContact().observe(this, this::onImportContact);
        mViewModel.onCommonError().observe(this, this::onCommonError);
        mViewModel.progress().observe(this, this::progress);
        mViewModel.onCurrentAddress().observe(this, this::onCurrentAddress);
    }

    private void onCurrentAddress(String address) {
        this.wallet = new Wallet(address);
        mViewModel.getAllContacts(wallet);
    }

    private void onImportContact(Boolean aBoolean) {
        toast(R.string.import_contact_success);
        needImport = false;
        mViewModel.getAllContacts(wallet);
    }

    private void onExportContact(File file) {
        mViewModel.openShare(mContext, file);
    }

    private void onContactInfo(ArrayList<ContactsInfo> contactsInfos) {
        this.contactsInfos = contactsInfos;
        adapter.setContacts(contactsInfos);
        String[] firstPinyin = adapter.getFirstPinyin();
        if (firstPinyin != null && firstPinyin.length > 0) {
            indexView.setVisibility(View.VISIBLE);
            indexView.setIndexStrings(firstPinyin);
        } else {
            indexView.setVisibility(View.GONE);
        }
        if (null == contactsInfos || contactsInfos.size() == 0) {
            emptyRelativeLayout.setVisibility(View.VISIBLE);
        } else {
            emptyRelativeLayout.setVisibility(View.GONE);
        }
        if(needImport) {
            if(needImportFile != null) {
                mViewModel.importContact(wallet, new File(needImportFile));
            }
        }
    }

    private void onDeleteContact(Boolean aBoolean) {
        mViewModel.getAllContacts(wallet);
    }

    @Override
    protected void injectActivity() {
        AndroidInjection.inject(this);
    }

    protected void initView() {
        addressBookActivity = this;
        rxPermissions = new RxPermissions(this);
        addAddressBt.setOnClickListener(this);
        adapter = new ContactsAdapter(clickedAddress -> {
            Intent intent = new Intent();
            intent.putExtra(C.EXTRA_ADDRESS, clickedAddress);
            if (SOURCE_CODE == SOURCE_SEND) {
                setResult(RESULT_OK, intent);
                finish();
            } else if (SOURCE_CODE == SOURCE_ME) {
                if (addressBookActivity != null) {
                    new SendRouter().openWithAddress(addressBookActivity, clickedAddress, null);
                }
            }

        }, (deletedAddress, parent) -> {
            DeleteItemPopuwindow p = new DeleteItemPopuwindow(this);
            p.setOnDeleteFriendListener(() -> {
                mViewModel.deleteContact(wallet, deletedAddress);
                p.dismiss();
            });
            p.setOnUpdateFriendListener(() -> {
                new UpdateFriendRouter().openForResult(addressBookActivity, C.REQUEST_CODE_UPDATE_FRIEND, wallet, deletedAddress);
                p.dismiss();
            });
            p.showAsDropDown(parent, parent.getWidth() / 2, -parent.getHeight() / 5);
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        indexView.setOnChooseListener(new IndexView.ChooseListener() {
            @Override
            public void onChoose(int pos, String text) {
                int positionByHeader = adapter.getPositionByHeader(text);
                if (positionByHeader != -1) {
                    layoutManager.scrollToPositionWithOffset(adapter.getPositionByHeader(text), 0);
                }
            }
        });
        friendsListView.setLayoutManager(layoutManager);
        friendsListView.setAdapter(adapter);
        friendsListView.addItemDecoration(new SampleItemDecoration(this));
        friendsListView.setHasFixedSize(true);
        ColorStateList backgroundTintList = addAddressBt.getBackgroundTintList();

        addAddressBt.setBackgroundTintList(getResources().getColorStateList(R.color.fab_selector));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.getCurrentAddress();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_share) {
            showMorePopupWindow();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMorePopupWindow() {
        ArrayList<String> list = new ArrayList<>();
        list.add(getString(R.string.import_addresses));
        list.add(getString(R.string.export_addresses));
        morePopupWindow = new MorePopupWindow(mContext, list, data -> {
            int i = list.indexOf(data);
            switch (i) {
                case 0:
                    importContact();
                    break;
                case 1:
                    if(contactsInfos.size() > 0) {
                        exportContact();
                    }else {
                        toast(getString(R.string.address_is_empty));
                    }
                    break;
            }
            morePopupWindow.dismiss();
        });
        morePopupWindow.showAsDropDown(popRootView, 0, 0, Gravity.END);
    }

    private void importContact() {
        Disposable subscribe = rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(
                        granted -> {
                            if (granted) {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("*/*");
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_file)), FILE_SELECT_CODE);

                            } else {
                                toast(R.string.write_external_permission_denied);
                            }
                        }
                );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    Logger.e("uri:" + uri.toString());
                    String path = FileUtils.getPath(mContext, uri);
                    Logger.e("Path:" + path);
                    mViewModel.importContact(wallet, new File(path));
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    private void exportContact() {
        Disposable subscribe = rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(
                        granted -> {
                            if (granted) {
                                String saveFileName = "contact-" +NewAddressUtils.hexAddress2NewAddress(wallet.address) + ".vcf";
                                String path = C.CONTACT_DIR + File.separator + saveFileName;
                                File file = new File(path);
                                mViewModel.exportContact(wallet, file);
                            } else {
                                toast(R.string.write_external_permission_denied);
                            }
                        }
                );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addAddressBt:
                new AddFriendRouter().open(this, wallet);
                break;
        }
    }
}
