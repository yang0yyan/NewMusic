package com.yy.music.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yy.music.base.BaseFragment;
import com.yy.music.databinding.FragmentOnlineBinding;

public class OnlineFragment extends BaseFragment {
    private FragmentOnlineBinding binding;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentOnlineBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
