require 'spec_helper'
describe 'tomtopweb' do

  context 'with defaults for all parameters' do
    it { should contain_class('tomtopweb') }
  end
end
